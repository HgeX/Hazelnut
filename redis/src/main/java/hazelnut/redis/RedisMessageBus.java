package hazelnut.redis;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import hazelnut.core.MessageBus;

import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;

public final class RedisMessageBus implements MessageBus {
    private final Logger LOGGER = logger(RedisMessageBus.class);
    private final String name;
    private final JedisPool pool;

    RedisMessageBus(final @NotNull String name,
                    final @NotNull JedisPool pool) {
        this.name = name;
        this.pool = pool;
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
    public void close() {
        this.pool.destroy();
    }
}
