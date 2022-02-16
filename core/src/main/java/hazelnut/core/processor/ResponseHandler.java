package hazelnut.core.processor;

import hazelnut.core.Hazelnut;
import hazelnut.core.HazelnutMessage;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;
import static java.util.Objects.requireNonNull;

public final class ResponseHandler {
    private static final Logger LOGGER = logger(ResponseHandler.class);
    private final Hazelnut hazelnut;

    public ResponseHandler(final @NotNull Hazelnut hazelnut) {
        this.hazelnut = requireNonNull(hazelnut, "hazelnut cannot be null");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull Optional<Message<?>> runProcessors(final @NotNull MessageContext<?> context) {
        final HazelnutMessage<?> message = context.message();
        final List<MessageProcessor<?>> processors = this.hazelnut.processors().find(message.data().type());
        if (processors.isEmpty()) {
            return Optional.empty();
        }

        for (final MessageProcessor each : processors) {
            try {
                final Message<?> response = each.process(context);
                if (response instanceof Response.NoOp) {
                    return Optional.empty();
                } else if (!(response instanceof Response.Next)) {
                    return Optional.of(response);
                }

            } catch (final Throwable ex) {
                LOGGER.warning("Message processor %s threw an exception while processing message %s"
                        .formatted(each, message));
                ex.printStackTrace();
            }
        }

        LOGGER.warning("ERROR! None of the registered processors returned a valid response for message %s"
                .formatted(message));
        return Optional.empty();
    }

    public void respond(final @NotNull HazelnutMessage<?> message) {
        final MessageContext<?> context = new MessageContextImpl<>(message);
        final Optional<Message<?>> response = runProcessors(context);
        if (response.isEmpty()) {
            return;
        }

        this.hazelnut.to(message.header().originId())
                .send(response.orElseThrow());
    }
}
