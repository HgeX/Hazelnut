package hazelnut.core.translation;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import hazelnut.core.HazelnutMessage;
import hazelnut.core.Message;
import hazelnut.core.MessageHeader;
import hazelnut.core.translation.builtin.ResponseTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public final class TranslatorRegistryImpl implements TranslatorRegistry {
    private final Map<Class<?>, MessageTranslator<?>> translators = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public TranslatorRegistryImpl() {
        add(new ResponseTranslator());
    }

    @Override
    public void add(final @NotNull MessageTranslator<?> translator) {
        try {
            this.lock.lock();
            final Class<?> type = translator.type();
            if (this.translators.containsKey(type)) {
                throw new IllegalArgumentException("MessageTranslator with type %s is already registered".formatted(type));
            }

            this.translators.put(type, translator);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean replace(final @NotNull MessageTranslator<?> translator) {
        try {
            this.lock.lock();
            return this.translators.put(translator.type(), translator) != null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void remove(final @NotNull MessageTranslator<?> translator) {
        try {
            this.lock.lock();
            this.translators.remove(translator.type());
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Set<MessageTranslator<?>> entries() {
        try {
            this.lock.lock();
            return Set.copyOf(this.translators.values());
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> Optional<MessageTranslator<T>> find(final @NotNull Class<T> type) {
        try {
            this.lock.lock();
            final MessageTranslator<T> translator = (MessageTranslator<T>) this.translators.get(type);
            return Optional.ofNullable(translator);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull String stringify(final @NotNull HazelnutMessage<?> message) throws TranslationException {
        final MessageHeader header = message.header();
        final Message<?> data = message.data();
        final MessageTranslator translator = find(data.type())
                .orElseThrow(() -> new IllegalStateException(
                        "Could not find translator for message %s".formatted(data)));
        final JsonObject obj = new JsonObject()
                .add("originId", header.originId())
                .add("messageId", header.messageId().toString())
                .add("type", header.type().getName());
        final JsonObject intermediary = translator.toIntermediary(data);
        obj.add("data", intermediary);

        return obj.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T extends Message<T>> HazelnutMessage<T> parse(final @NotNull String message) throws TranslationException {
        final JsonValue json = Json.parse(message);
        if (!json.isObject()) {
            throw new IllegalStateException("Received corrupted (must be a valid JSON object): %s".formatted(message));
        }

        final JsonObject obj = json.asObject();
        final String originId = obj.get("originId").asString();
        final String messageIdStr = obj.get("messageId").asString();
        final UUID messageId;
        try {
            messageId = UUID.fromString(messageIdStr);
        } catch (final IllegalArgumentException ex) {
            throw new RuntimeException("Corrupted uuid detected: %s".formatted(messageIdStr), ex);
        }

        final String typeStr = obj.get("type").asString();
        final Class<T> type;
        try {
            type = (Class<T>) Class.forName(typeStr);
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException("Could not load class %s".formatted(typeStr), ex);
        }

        final MessageTranslator<T> translator = find(type)
                .orElseThrow(() -> new IllegalStateException("Could not find message translator for type %s"
                        .formatted(typeStr)));

        final T data = translator.fromIntermediary(obj.get("data").asObject());
        final MessageHeader header = MessageHeader.of( messageId, originId, type);
        return  new HazelnutMessage<>(header, data);
    }
}
