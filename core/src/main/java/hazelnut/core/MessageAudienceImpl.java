package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

final class MessageAudienceImpl implements MessageAudience {
    private final Executor executor;
    private final Set<MessageChannel> channels;

    MessageAudienceImpl(final @NotNull Executor executor,
                        final @NotNull Set<MessageChannel> channels) {
        this.executor = requireNonNull(executor, "executor cannot be null");
        this.channels = requireNonNull(channels, "channels cannot be null");
    }

    @Override
    public @NotNull Set<MessageChannel> channels() {
        return Set.copyOf(this.channels);
    }

    @Override
    public @NotNull CompletableFuture<?> send(final @NotNull Message<?> message) {
        return CompletableFuture.runAsync(() -> this.channels.forEach(channel -> channel.send(message)),
                this.executor);
    }

    @Override
    public void sendNow(final @NotNull Message<?> message) {
        send(message).join();
    }
}
