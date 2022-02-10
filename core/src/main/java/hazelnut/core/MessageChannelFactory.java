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

    @NotNull MessageChannel createChannelWithId(final @NotNull String channelId) {
        final MessageBus bus = this.busFactory.create(channelId);
        bus.addListener(this.messageListener::consume);
        return new MessageChannelImpl(channelId, bus, this.translators);
    }
}
