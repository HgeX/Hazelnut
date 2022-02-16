package hazelnut.redis;

import hazelnut.core.MessageBus;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;

public final class RedisMessageBus implements MessageBus {
    private final Logger LOGGER = logger(RedisMessageBus.class);
    private final Set<Subscriber> subscribers = new HashSet<>();
    private final String name;
    private final JedisPool pool;
    private final Executor executor;

    RedisMessageBus(final @NotNull String name,
                    final @NotNull JedisPool pool,
                    final @NotNull Executor executor) {
        this.name = name;
        this.pool = pool;
        this.executor = executor;
    }

    @Override
    public void deliver(final @NotNull String message) {
        try (final Jedis jedis = this.pool.getResource()) {
            jedis.publish(this.name, message);
        } catch (final Throwable ex) {
            LOGGER.warning("Encountered an unexpected exception while publishing data: %s".formatted(message));
            ex.printStackTrace();
        }
    }

    @Override
    public void addListener(final @NotNull Consumer<String> listener) {
        this.executor.execute(() -> {
            try (final Jedis jedis = this.pool.getResource()) {
                final Subscriber subscriber = new Subscriber(this.name, listener);
                jedis.subscribe(subscriber, this.name);
                this.subscribers.add(subscriber);
            } catch (final Throwable ex) {
                LOGGER.warning("Encountered an unexpected exception while subscribing to channel %s".formatted(this.name));
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void close() {
        this.subscribers.forEach(JedisPubSub::unsubscribe);
        this.pool.destroy();
    }

    private static final class Subscriber extends JedisPubSub {
        private final String name;
        private final Consumer<String> action;

        Subscriber(final @NotNull String name,
                   final @NotNull Consumer<String> action) {
            this.name = name;
            this.action = action;
        }

        @Override
        public void onMessage(final @NotNull String channel, final @NotNull String message) {
            if (!channel.equalsIgnoreCase(this.name)) {
                throw new IllegalStateException("Received message from an unrecognized channel (%s)".formatted(channel));
            }

            this.action.accept(message);
        }
    }
}
