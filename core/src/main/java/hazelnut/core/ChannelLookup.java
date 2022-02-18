package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface ChannelLookup extends AutoCloseable {

    @NotNull Optional<MessageChannel> find(final @NotNull String channelId);

    @NotNull Optional<MessageChannel> findStatic(final @NotNull String channelId);

    @NotNull Optional<MessageChannel> findVolatile(final @NotNull String channelId);

    @NotNull Set<MessageChannel> staticChannels();

    @NotNull Set<MessageChannel> volatileChannels();

    void registerStatic(final @NotNull MessageChannel channel);

    void registerVolatile(final @NotNull MessageChannel channel);

    @Override
    void close() throws Exception;
}
