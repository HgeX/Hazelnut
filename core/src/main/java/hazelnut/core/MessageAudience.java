package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface MessageAudience {
    MessageAudience EMPTY = EmptyAudience.INSTANCE;

    @NotNull Set<MessageChannel> channels();

    @NotNull CompletableFuture<?> send(final @NotNull Message<?> message);

    void sendNow(final @NotNull Message<?> message);
}
