package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MessageAudience {
    MessageAudience EMPTY = EmptyAudience.INSTANCE;

    @NotNull Set<MessageChannel> channels();

    void send(final @NotNull Message<?> message);
}
