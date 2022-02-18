package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageChannelFactory {

    @NotNull MessageChannel.Inbound inbound(final @NotNull String channelId);

    @NotNull MessageChannel.Outbound outbound(final @NotNull String channelId);

    @NotNull MessageChannel.Duplex duplex(final @NotNull String channelId);
}
