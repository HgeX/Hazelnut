package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface MessageBus extends AutoCloseable {

    void deliver(final @NotNull String message);

    void addListener(final @NotNull Consumer<String> listener);

    @Override
    void close() throws Exception;
}
