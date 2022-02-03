package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;
import static hazelnut.core.util.Miscellaneous.toNamespaced;

final class ChannelLookupImpl implements ChannelLookup {
    private final Map<String, MessageChannel> knownChannels = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Namespace namespace;
    private final MessageChannelFactory channelFactory;

    ChannelLookupImpl(final @NotNull Namespace namespace,
                              final @NotNull MessageChannelFactory channelFactory) {
        this.namespace = requireNonNull(namespace, "namespace cannot be null");
        this.channelFactory = requireNonNull(channelFactory, "channelFactory cannot be null");
    }

    @Override
    public @NotNull Optional<MessageChannel> find(final @NotNull String channelId) {
        try {
            this.lock.lock();
            final @Nullable MessageChannel channel = this.knownChannels.get(toNamespaced(this.namespace, channelId));
            return Optional.ofNullable(channel);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Set<MessageChannel> channels() {
        try {
            this.lock.lock();
            return Set.copyOf(this.knownChannels.values());
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void register(final @NotNull String channelId) {
        try {
            this.lock.lock();
            final String actualId = toNamespaced(this.namespace, channelId);
            if (this.knownChannels.containsKey(actualId)) {
                throw new IllegalStateException("MessageChannel with id %s is already registerd.".formatted(actualId));
            }

            this.knownChannels.put(actualId, this.channelFactory.createChannelWithId(actualId));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void register(final @NotNull MessageChannel channel) {
        try {
            this.lock.lock();
            final String id = channel.channelId();
            if (this.knownChannels.containsKey(id)) {
                throw new IllegalStateException("MessageChannel with id %s is already registerd.".formatted(id));
            }

            this.knownChannels.put(id, channel);
        } finally {
            this.lock.unlock();
        }
    }
}
