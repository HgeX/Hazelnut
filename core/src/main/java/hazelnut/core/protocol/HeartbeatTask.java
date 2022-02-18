package hazelnut.core.protocol;

import hazelnut.core.Hazelnut;
import hazelnut.core.Namespace;
import hazelnut.core.config.HazelnutConfig;
import hazelnut.core.util.Miscellaneous;
import hazelnut.core.util.NamedThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

public final class HeartbeatTask implements AutoCloseable {
    private static final String THREAD_NAME_FORMAT = "hazelnut-heartbeat";
    private final ActualTask task;
    private final ExecutorService executor;

    public HeartbeatTask(final @NotNull Hazelnut hazelnut,
                         final @NotNull HazelnutConfig config,
                         final @NotNull Namespace namespace) {
        this.task = new ActualTask(
                requireNonNull(hazelnut, "hazelnut cannot be null"),
                requireNonNull(config, "config cannot be null")
        );

        requireNonNull(namespace, "namespace cannot be null");
        this.executor = Executors.newSingleThreadExecutor(new NamedThreadFactory(
                namespace.format(THREAD_NAME_FORMAT)
        ));
    }

    public void start() {
        this.executor.execute(this.task);
    }

    @Override
    public void close() {
        this.task.running = false;
        Miscellaneous.shutdownExecutor(this.executor);
    }

    @SuppressWarnings("all")
    private static final class ActualTask implements Runnable {
        private final Hazelnut hazelnut;
        private final HazelnutConfig config;
        private boolean running = true;

        private ActualTask(final @NotNull Hazelnut hazelnut,
                           final @NotNull HazelnutConfig config) {
            this.hazelnut = hazelnut;
            this.config = config;
        }

        @Override
        public void run() {
            while (this.running) {
                try {
                    this.hazelnut.everyone().send(new Heartbeat());
                    Thread.sleep(this.config.heartbeatRate());
                } catch (final InterruptedException ignored) {}
            }
        }
    }
}
