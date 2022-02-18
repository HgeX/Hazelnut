package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.util.Objects.requireNonNull;

final class MessageAudienceImpl implements MessageAudience {
    private final Set<MessageChannel.Outbound> channels;
    private final String identity;

    MessageAudienceImpl(final @NotNull Set<MessageChannel.Outbound> channels,
                        final @NotNull String identity) {
        this.channels = requireNonNull(channels, "channels cannot be null");
        this.identity = requireNonNull(identity, "identity cannot be null");
    }

    @Override
    public @NotNull Set<MessageChannel.Outbound> channels() {
        return Set.copyOf(this.channels);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void send(final @NotNull Message<?> message) {
        final HazelnutMessage hazelnutMessage = new HazelnutMessage(
                new MessageHeaderImpl(this.identity, message.type()),
                message
        );

        this.channels.forEach(channel -> channel.send(hazelnutMessage));
    }
}
