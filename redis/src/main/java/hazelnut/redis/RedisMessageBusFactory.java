package hazelnut.redis;

import hazelnut.core.MessageBus;
import hazelnut.core.MessageBusFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.concurrent.Executor;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.isNullOrEmpty;
import static hazelnut.core.util.Miscellaneous.logger;
import static java.util.Objects.requireNonNull;

public final class RedisMessageBusFactory implements MessageBusFactory {
    private static final Logger LOGGER = logger(RedisMessageBusFactory.class);
    private final JedisPool pool;
    private final Executor executor;

    private RedisMessageBusFactory(final @NotNull JedisPool pool,
                                   final @NotNull Executor executor) {
        this.pool = requireNonNull(pool, "pool cannot be null");
        this.executor = requireNonNull(executor, "executor cannot be null");
    }

    @Override
    public @NotNull MessageBus create(final @NotNull String name) {
        LOGGER.info("Creating message channel with id: %s".formatted(name));
        return new RedisMessageBus(name, this.pool, this.executor);
    }

    public static @NotNull RedisMessageBusFactory using(final @NotNull JedisPool pool, final @NotNull Executor executor) {
        return new RedisMessageBusFactory(pool, executor);
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String host;
        private int port;
        private String username;
        private String password;
        private Executor executor;

        private Builder() {}

        public @NotNull Builder host(final @NotNull String host) {
            this.host = host;
            return this;
        }

        public @NotNull Builder port(final int port) {
            this.port = port;
            return this;
        }

        public @NotNull Builder username(final @NotNull String username) {
            this.username = username;
            return this;
        }

        public @NotNull Builder password(final @NotNull String password) {
            this.password = password;
            return this;
        }

        public @NotNull Builder executor(final @NotNull Executor executor) {
            this.executor = executor;
            return this;
        }

        public @NotNull RedisMessageBusFactory build() {
            final String host = requireNonNull(this.host, "host cannot be null");
            final Executor executor = requireNonNull(this.executor, "executor cannot be null");
            final int port = this.port;
            final @Nullable String username = this.username;
            final @Nullable String password = this.password;
            final JedisPool pool;

            if (isNullOrEmpty(username) || isNullOrEmpty(password)) {
                pool = new JedisPool(new JedisPoolConfig(), host, port);
            } else {
                pool = new JedisPool(
                        new JedisPoolConfig(),
                        host,
                        port,
                        Protocol.DEFAULT_TIMEOUT,
                        username,
                        password
                );
            }

            return new RedisMessageBusFactory(pool, executor);
        }
    }
}
