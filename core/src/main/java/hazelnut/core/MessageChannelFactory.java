package hazelnut.core;

import hazelnut.core.processor.IncomingMessageListener;
import hazelnut.core.translation.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class MessageChannelFactory {
    private final MessageBusFactory busFactory;
    private final TranslatorRegistry translators;
    private final IncomingMessageListener messageListener;

    MessageChannelFactory(final @NotNull MessageBusFactory busFactory,
                          final @NotNull TranslatorRegistry translators,
                          final @NotNull IncomingMessageListener messageListener) {
        this.busFactory = requireNonNull(busFactory, "busFactory cannot be null");
        this.translators = requireNonNull(translators, "translators cannot be null");
        this.messageListener = requireNonNull(messageListener, "messageListener cannot be null");
    }

    @NotNull MessageChannel createChannel(final @NotNull String channelId, final boolean addListener) {
        final MessageBus bus = this.busFactory.create(channelId);
        if (addListener) {
            bus.addListener(this.messageListener::consume);
        }

        return new MessageChannelImpl(channelId, bus, this.translators);
    }

    @NotNull MessageChannel createInbound(final @NotNull String channelId) {
        final MessageBus bus = this.busFactory.create(channelId);
        bus.addListener(this.messageListener::consume);
        return new MessageChannelImpl.Inbound(channelId, bus, this.translators);
    }

    @NotNull MessageChannel createOutbound(final @NotNull String channelId) {
        final MessageBus bus = this.busFactory.create(channelId);
        return new MessageChannelImpl.Outbound(channelId, bus, this.translators);
    }
}
