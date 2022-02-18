package hazelnut.core;

import hazelnut.core.config.HazelnutConfig;
import hazelnut.core.util.Cache;
import hazelnut.core.util.NamedThreadFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;
import static hazelnut.core.util.Miscellaneous.toNamespaced;
import static java.util.Objects.requireNonNull;

public final class ChannelLookupImpl implements ChannelLookup {
    private static final String CACHE_HOUSEKEEPER_NAME = "hazelnut-cache-housekeeper #%d";
    private static final Logger LOGGER = logger(ChannelLookupImpl.class);
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, MessageChannel> staticChannels = new HashMap<>();
    private final Cache<String, MessageChannel> volatileChannels;
    private final Namespace namespace;
    private final MessageChannelFactory channelFactory;

    ChannelLookupImpl(final @NotNull Namespace namespace,
                      final @NotNull MessageChannelFactory channelFactory,
                      final @NotNull HazelnutConfig config) {
        this.namespace = requireNonNull(namespace, "namespace cannot be null");
        this.channelFactory = requireNonNull(channelFactory, "channelFactory cannot be null");
        requireNonNull(config, "config cannot be null");
        final ScheduledExecutorService cacheHousekeeper = Executors.newScheduledThreadPool(
                1,
                new NamedThreadFactory(namespace.format(CACHE_HOUSEKEEPER_NAME))
        );
        this.volatileChannels = Cache.<String, MessageChannel>builder()
                .lifetime(config.cacheExpiryRate())
                .evictionListener(this::onEviction)
                .housekeeperRate(config.cacheHouskeeperRate())
                .housekeeper(cacheHousekeeper)
                .build();
    }

    private void onEviction(final @NotNull MessageChannel channel) {
        try {
            LOGGER.info("Closing message channel %s".formatted(channel.channelId()));
            channel.close();
        } catch (final Throwable ex) {
            LOGGER.warning("Encountered an unexpected exception while closing channel with id %s".formatted(
                    channel.channelId()
            ));

            ex.printStackTrace();
        }
    }

    @Override
    public @NotNull Optional<MessageChannel> find(final @NotNull String channelId) {
        return findStatic(channelId).or(() -> findVolatile(channelId));
    }

    @Override
    public @NotNull Optional<MessageChannel> findStatic(final @NotNull String channelId) {
        try {
            this.lock.lock();
            final @Nullable MessageChannel channel = this.staticChannels.get(toNamespaced(this.namespace, channelId));
            return Optional.ofNullable(channel);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Optional<MessageChannel> findVolatile(final @NotNull String channelId) {
        return this.volatileChannels.findByKey(toNamespaced(this.namespace, channelId));
    }

    @Override
    public @NotNull Set<MessageChannel> staticChannels() {
        try {
            this.lock.lock();
            return Set.copyOf(this.staticChannels.values());
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Set<MessageChannel> volatileChannels() {
        return Set.copyOf(this.volatileChannels.values());
    }

    @Override
    public void registerStatic(final @NotNull MessageChannel channel) {
        try {
            this.lock.lock();
            final String id = channel.channelId();
            if (this.staticChannels.containsKey(id)) {
                throw new IllegalStateException("MessageChannel with id %s is already registered.".formatted(id));
            }

            this.staticChannels.put(id, channel);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void registerVolatile(final @NotNull MessageChannel channel) {
        final String id = channel.channelId();
        if (this.volatileChannels.findByKey(id).isPresent()) {
            throw new IllegalStateException("MessageChannel with id %s is already registered.".formatted(id));
        }

        this.volatileChannels.cache(id, channel);
    }

    @Override
    public void close() throws Exception {
        this.staticChannels.clear();
        this.volatileChannels.close();
    }

    public void updateVolatileChannel(final @NotNull String channelId, final boolean subscribe) {
        if (this.volatileChannels.findByKey(channelId).isPresent()) {
            this.volatileChannels.rebirth(channelId);
        } else {
            final MessageChannel channel = subscribe
                    ? this.channelFactory.inbound(channelId)
                    : this.channelFactory.outbound(channelId);
            this.volatileChannels.cache(channelId, channel);
        }
    }
}
