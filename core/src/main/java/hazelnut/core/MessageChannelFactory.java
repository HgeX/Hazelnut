package hazelnut.core;

import hazelnut.core.translation.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class MessageChannelFactory {
    private final MessageBusFactory busFactory;
    private final TranslatorRegistry translators;

    MessageChannelFactory(final @NotNull MessageBusFactory busFactory,
                          final @NotNull TranslatorRegistry translators) {
        this.busFactory = requireNonNull(busFactory, "busFactory cannot be null");
        this.translators = requireNonNull(translators, "translators cannot be null");
    }

    @NotNull MessageChannel createChannelWithId(final @NotNull String channelId) {
        return new MessageChannelImpl(channelId, this.busFactory.create(channelId), this.translators);
    }
}
