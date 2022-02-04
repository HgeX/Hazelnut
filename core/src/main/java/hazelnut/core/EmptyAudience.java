package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;

final class EmptyAudience implements MessageAudience {
    private static final Logger LOGGER = logger(EmptyAudience.class);
    static final MessageAudience INSTANCE = new EmptyAudience();

    private EmptyAudience() {}

    @Override
    public @NotNull Set<MessageChannel> channels() {
        return Set.of();
    }

    @Override
    public @NotNull CompletableFuture<?> send(final @NotNull Message<?> message) {
        sendNow(message);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void sendNow(final @NotNull Message<?> message) {
        LOGGER.warning("Attempted to send message to empty audience: %s".formatted(message));
    }
}