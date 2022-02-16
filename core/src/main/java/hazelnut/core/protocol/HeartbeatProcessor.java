package hazelnut.core.protocol;

import hazelnut.core.ChannelLookupImpl;
import hazelnut.core.Hazelnut;
import hazelnut.core.Message;
import hazelnut.core.Namespace;
import hazelnut.core.processor.MessageContext;
import hazelnut.core.processor.MessageProcessor;
import hazelnut.core.processor.Response;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public final class HeartbeatProcessor implements MessageProcessor<Heartbeat> {
    private final String identity;
    private final ChannelLookupImpl channelLookup;
    private final Namespace namespace;

    public HeartbeatProcessor(final @NotNull String identity,
                              final @NotNull ChannelLookupImpl channelLookup,
                              final @NotNull Namespace namespace) {
        this.identity = requireNonNull(identity, "identity cannot be null");
        this.channelLookup = requireNonNull(channelLookup, "channelLookup cannot be null");
        this.namespace = requireNonNull(namespace, "namespace cannot be null");
    }

    @Override
    public @NotNull Class<Heartbeat> messageType() {
        return Heartbeat.class;
    }

    @Override
    public @NotNull Message<?> process(final @NotNull MessageContext<Heartbeat> context) {
        final String origin = context.message().header().originId();
        // Ignore our own messages
        if (!origin.equalsIgnoreCase(this.identity)) {
            final String toChannel = this.namespace.format(this.identity + Hazelnut.PARTICIPANT_DELIMITER + origin);
            final String fromChannel = this.namespace.format(origin + Hazelnut.PARTICIPANT_DELIMITER + this.identity);

            this.channelLookup.updateVolatileChannel(toChannel, false);
            this.channelLookup.updateVolatileChannel(fromChannel, true);
        }

        return Response.noop();
    }
}
