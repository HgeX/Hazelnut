package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

public interface MessageProcessor<T extends Message<T>> {

    @NotNull TypeToken<T> messageType();

    @NotNull Message<?> process(final @NotNull MessageContext<T> context);
}
