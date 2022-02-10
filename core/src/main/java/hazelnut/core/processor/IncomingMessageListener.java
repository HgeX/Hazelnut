package hazelnut.core.processor;

import hazelnut.core.HazelnutMessage;
import hazelnut.core.translation.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;
import static java.util.Objects.requireNonNull;

public final class IncomingMessageListener {
    private static final Logger LOGGER = logger(IncomingMessageListener.class);
    private final TranslatorRegistry translators;
    private final ResponseHandler responseHandler;

    public IncomingMessageListener(final @NotNull TranslatorRegistry translators,
                                   final @NotNull ResponseHandler responseHandler) {
        this.translators = requireNonNull(translators, "translators cannot be null");
        this.responseHandler = requireNonNull(responseHandler, "responseHandler cannot be null");
    }

    public void consume(final @NotNull String data) {
        try {
            final HazelnutMessage<?> message = this.translators.parse(data);
            this.responseHandler.respond(message);

        } catch (final Throwable ex) {
            LOGGER.warning("Encountered an unexpected exception while consuming incoming message:");
            LOGGER.warning(data);
            ex.printStackTrace();
        }
    }
}
