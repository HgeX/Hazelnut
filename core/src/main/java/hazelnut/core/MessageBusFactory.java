package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public interface MessageBusFactory extends AutoCloseable {

    @NotNull String name();

    @NotNull MessageBus create(final @NotNull String name, final @NotNull Executor executor);

    @Override
    void close() throws Exception;
}
