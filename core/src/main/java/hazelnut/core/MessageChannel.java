package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageChannel extends AutoCloseable {

    @NotNull String channelId();

    void send(final @NotNull PreparedMessage<?> message);

    @Override
    void close() throws Exception;
}
