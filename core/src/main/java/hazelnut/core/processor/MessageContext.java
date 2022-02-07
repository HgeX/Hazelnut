package hazelnut.core.processor;

import hazelnut.core.HazelnutMessage;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageContext<T extends Message<T>> {

    @NotNull HazelnutMessage<T> message();

    @NotNull Response failed();

    @NotNull Response failed(final @NotNull Throwable cause);

    @NotNull Response success();
}
