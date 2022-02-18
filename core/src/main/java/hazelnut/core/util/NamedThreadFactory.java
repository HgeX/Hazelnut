package hazelnut.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public final class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger taskId = new AtomicInteger(0);
    private final String nameFormat;

    public NamedThreadFactory(final @NotNull String nameFormat) {
        this.nameFormat = requireNonNull(nameFormat, "nameFormat cannot be null");
    }

    @Override
    public Thread newThread(final @NotNull Runnable task) {
        final Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.setName(this.nameFormat.formatted(this.taskId.incrementAndGet()));
        return thread;
    }
}
