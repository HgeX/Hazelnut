package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageChannel extends AutoCloseable {

    @NotNull String channelId();

    void send(final @NotNull HazelnutMessage<?> message);

    @Override
    void close() throws Exception;
}
