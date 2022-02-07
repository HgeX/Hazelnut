package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import hazelnut.core.Message;
import hazelnut.core.PreparedMessage;
import org.jetbrains.annotations.NotNull;

public interface MessageProcessor<T extends Message<T>> {

    @NotNull TypeToken<T> messageType();

    @NotNull Message<?> process(final @NotNull PreparedMessage<T> message);
}
