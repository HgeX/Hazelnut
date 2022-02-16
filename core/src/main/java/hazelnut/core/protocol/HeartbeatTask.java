package hazelnut.core.protocol;

import hazelnut.core.Hazelnut;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;

public final class HeartbeatTask implements AutoCloseable {
    private static final int TIMEOUT_MILLIS = 5000;
    private final ActualTask task;
    private final Executor executor;

    public HeartbeatTask(final @NotNull Hazelnut hazelnut,
                         final @NotNull Executor executor) {
        this.executor = requireNonNull(executor, "executor cannot be null");
        this.task = new ActualTask(requireNonNull(hazelnut, "hazelnut cannot be null"));
    }

    public void start() {
        this.executor.execute(this.task);
    }

    @Override
    public void close() {
        this.task.running = false;
    }

    @SuppressWarnings("all")
    private static final class ActualTask implements Runnable {
        private final Hazelnut hazelnut;
        private boolean running = true;

        private ActualTask(final @NotNull Hazelnut hazelnut) {
            this.hazelnut = hazelnut;
        }

        @Override
        public void run() {
            while (this.running) {
                try {
                    this.hazelnut.everyone().send(new Heartbeat());
                    Thread.sleep(TIMEOUT_MILLIS);
                } catch (final InterruptedException ignored) {}
            }
        }
    }
}
