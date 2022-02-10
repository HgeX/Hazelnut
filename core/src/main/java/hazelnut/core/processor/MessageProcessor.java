package hazelnut.core.processor;

import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageProcessor<T extends Message<T>> {

    @NotNull Class<T> messageType();

    @NotNull Message<?> process(final @NotNull MessageContext<T> context);
}
