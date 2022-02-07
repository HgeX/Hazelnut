package hazelnut.core.processor;

import hazelnut.core.HazelnutMessage;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

final class MessageContextImpl<T extends Message<T>> implements MessageContext<T> {
    private final HazelnutMessage<T> message;

    MessageContextImpl(final @NotNull HazelnutMessage<T> message) {
        this.message = requireNonNull(message, "message cannot be null");
    }

    @Override
    public @NotNull HazelnutMessage<T> message() {
        return this.message;
    }

    @Override
    public @NotNull Response failed() {
        return new FailedResponse(originalId(), null, null);
    }

    @Override
    public @NotNull Response failed(final @NotNull Throwable cause) {
        return new FailedResponse(originalId(), cause.getClass().getName(), cause.getMessage());
    }

    @Override
    public @NotNull Response success() {
        return new SuccessfulResponse(originalId());
    }

    private @NotNull UUID originalId() {
        return this.message.header().messageId();
    }
}
