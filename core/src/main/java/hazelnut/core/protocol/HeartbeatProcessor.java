package hazelnut.core.protocol;

import hazelnut.core.ChannelLookupImpl;
import hazelnut.core.HazelnutImpl;
import hazelnut.core.Message;
import hazelnut.core.MessageChannel;
import hazelnut.core.processor.MessageContext;
import hazelnut.core.processor.MessageProcessor;
import hazelnut.core.processor.Response;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public final class HeartbeatProcessor implements MessageProcessor<Heartbeat> {
    private final String identity;
    private final ChannelLookupImpl channelLookup;
    private final BiFunction<String, Boolean, MessageChannel> channelFactory;

    public HeartbeatProcessor(final @NotNull String identity,
                              final @NotNull ChannelLookupImpl channelLookup,
                              final @NotNull BiFunction<String, Boolean, MessageChannel> channelFactory) {
        this.identity = requireNonNull(identity, "identity cannot be null");
        this.channelLookup = requireNonNull(channelLookup, "channelLookup cannot be null");
        this.channelFactory = requireNonNull(channelFactory, "channelFactory cannot be null");
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
            final String toChannel = this.identity + HazelnutImpl.PARTICIPANT_DELIMITER + origin;
            final String fromChannel = origin + HazelnutImpl.PARTICIPANT_DELIMITER + this.identity;

            this.channelLookup.updateVolatileChannel(this.channelFactory.apply(toChannel, false));
            this.channelLookup.updateVolatileChannel(this.channelFactory.apply(fromChannel, true));
        }

        return Response.noop();
    }
}
