package hazelnut.core.translation;

import com.eclipsesource.json.JsonObject;
import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import hazelnut.core.MessageHeader;
import hazelnut.core.HazelnutMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public final class TranslatorRegistryImpl implements TranslatorRegistry {
    private final Map<TypeToken<?>, MessageTranslator<?>> translators = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void add(final @NotNull MessageTranslator<?> translator) {
        try {
            this.lock.lock();
            final TypeToken<?> type = translator.type();
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
    public @NotNull <T> Optional<MessageTranslator<T>> find(final @NotNull TypeToken<T> type) {
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
                .add("messageId", header.messageId().toString());
        final JsonObject intermediary = translator.toIntermediary(data);
        obj.add("data", intermediary);

        return obj.toString();
    }
}
