package hazelnut.core;

import hazelnut.core.translation.TranslationException;
import hazelnut.core.translation.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;
import static java.util.Objects.requireNonNull;

final class MessageChannelImpl implements MessageChannel {
    private static final Logger LOGGER = logger(MessageChannelImpl.class);
    private final String id;
    private final MessageBus messageBus;
    private final TranslatorRegistry translators;

    MessageChannelImpl(final @NotNull String id,
                       final @NotNull MessageBus messageBus,
                       final @NotNull TranslatorRegistry translators) {
        this.id = requireNonNull(id, "id cannot be null");
        this.messageBus = requireNonNull(messageBus, "messageBus cannot be null");
        this.translators = requireNonNull(translators, "translators cannot be null");
    }

    @Override
    public @NotNull String channelId() {
        return this.id;
    }

    @Override
    public void send(final @NotNull HazelnutMessage<?> message) {
        try {
            final String finalMessage = this.translators.stringify(message);
            this.messageBus.deliver(finalMessage);

        } catch (final TranslationException ex) {
            LOGGER.warning("Could not translate data to intermediary type");
            LOGGER.warning(message.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.messageBus.close();
    }
}
