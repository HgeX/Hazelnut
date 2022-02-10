package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

final class MessageAudienceImpl implements MessageAudience {
    private final Executor executor;
    private final Set<MessageChannel> channels;
    private final String identity;

    MessageAudienceImpl(final @NotNull Executor executor,
                        final @NotNull Set<MessageChannel> channels,
                        final @NotNull String identity) {
        this.executor = requireNonNull(executor, "executor cannot be null");
        this.channels = requireNonNull(channels, "channels cannot be null");
        this.identity = requireNonNull(identity, "identity cannot be null");
    }

    @Override
    public @NotNull Set<MessageChannel> channels() {
        return Set.copyOf(this.channels);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void send(final @NotNull Message<?> message) {
        final HazelnutMessage hazelnutMessage = new HazelnutMessage(
                new MessageHeaderImpl(this.identity, message.type()),
                message
        );

        this.executor.execute(() -> this.channels.forEach(channel -> channel.send(hazelnutMessage)));
    }
}
