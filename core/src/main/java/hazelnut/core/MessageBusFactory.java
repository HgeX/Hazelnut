package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageBusFactory extends AutoCloseable {

    @NotNull MessageBus create(final @NotNull String name);

    @Override
    void close() throws Exception;
}
