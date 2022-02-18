package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface MessageAudience {
    MessageAudience EMPTY = EmptyAudience.INSTANCE;

    @NotNull Set<MessageChannel.Outbound> channels();

    void send(final @NotNull Message<?> message);
}
