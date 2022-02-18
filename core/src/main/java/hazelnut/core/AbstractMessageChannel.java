package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

abstract class AbstractMessageChannel implements MessageChannel {
    private final String channelId;
    protected final MessageBus messageBus;

    AbstractMessageChannel(final @NotNull String channelId,
                           final @NotNull MessageBus messageBus) {
        this.channelId = requireNonNull(channelId, "channelId cannot be null");
        this.messageBus = requireNonNull(messageBus, "messageBus cannot be null");
    }

    @Override
    public @NotNull String channelId() {
        return this.channelId;
    }

    @Override
    public void close() throws Exception {
        this.messageBus.close();
    }
}
