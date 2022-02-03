package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface ChannelLookup {

    @NotNull Optional<MessageChannel> find(final @NotNull String channelId);

    @NotNull Set<MessageChannel> channels();

    void register(final @NotNull String channelId);

    void register(final @NotNull MessageChannel channel);
}
