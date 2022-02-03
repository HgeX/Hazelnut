package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import hazelnut.core.translate.MessageTranslator;
import hazelnut.core.translate.TranslationException;
import hazelnut.core.translate.TranslatorCollection;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;
import static hazelnut.core.util.Miscellaneous.logger;

final class MessageChannelImpl implements MessageChannel {
    private static final Logger LOGGER = logger(MessageChannelImpl.class);
    private final String id;
    private final MessageBus messageBus;
    private final TranslatorCollection translators;

    MessageChannelImpl(final @NotNull String id,
                       final @NotNull MessageBus messageBus,
                       final @NotNull TranslatorCollection translators) {
        this.id = requireNonNull(id, "id cannot be null");
        this.messageBus = requireNonNull(messageBus, "messageBus cannot be null");
        this.translators = requireNonNull(translators, "translators cannot be null");
    }

    @Override
    public @NotNull String channelId() {
        return this.id;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void send(final @NotNull Message<?> message) {
        final MessageTranslator translator = this.translators.find(message.type()).orElseThrow(
                () -> new IllegalArgumentException("Could not find translator for object %s".formatted(message)));
        try {
            final String finalMessage = this.translators.finalize(translator.toIntermediary(message));
            this.messageBus.deliver(finalMessage);

        } catch (final TranslationException ex) {
            LOGGER.warning("Could not translate message to intermediary type");
            LOGGER.warning(message.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.messageBus.close();
    }
}
