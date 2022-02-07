package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import hazelnut.core.MessageHeader;
import hazelnut.core.PreparedMessage;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;

final class ResponseProcessor implements MessageProcessor<Response> {
    private static final Logger LOGGER = logger(ResponseProcessor.class);

    @Override
    public @NotNull TypeToken<Response> messageType() {
        return Response.TYPE;
    }

    @Override
    public @NotNull Message<?> process(final @NotNull PreparedMessage<Response> message) {
        final MessageHeader header = message.header();
        final Response response = message.data();
        if (!response.status()) {
            LOGGER.warning("Received a failed response from %s (message id: %s)"
                    .formatted(header.originId(), header.messageId()));
            if (!(response instanceof FailedResponse failed)) {
                throw new AssertionError("Response object is not an instance of %s"
                        .formatted(FailedResponse.class.getName()));
            }

            failed.errorClassName().ifPresent(x -> LOGGER.warning("The encountered error was of type %s"
                    .formatted(x)));
            failed.errorMessage().ifPresent(x -> LOGGER.warning("'%s'".formatted(x)));
        }

        return Response.noop();
    }
}
