package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageBus extends AutoCloseable {

    void deliver(final @NotNull String message);

    @Override
    void close() throws Exception;
}
