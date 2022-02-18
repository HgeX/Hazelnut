package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageChannel extends AutoCloseable {

    @NotNull String channelId();

    @Override
    void close() throws Exception;

    interface Inbound extends MessageChannel {}

    interface Outbound extends MessageChannel {

        void send(final @NotNull HazelnutMessage<?> message);
    }

    interface Duplex extends Outbound, Inbound {}
}
