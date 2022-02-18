package hazelnut.core.util;

import hazelnut.core.Hazelnut;
import hazelnut.core.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Miscellaneous {
    private Miscellaneous() {
        throw new UnsupportedOperationException();
    }

    public static @NotNull Logger logger(final @NotNull Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    public static boolean isNullOrEmpty(final @Nullable String value) {
        return value == null || value.isEmpty();
    }

    public static @NotNull String toNamespaced(final @NotNull Namespace namespace, final @NotNull String value) {
        if (value.contains(Namespace.DELIMITER)) {
            return value;
        }

        return namespace.format(value);
    }

    public static <T> void requireState(final @NotNull T t,
                                        final @NotNull BooleanFunction<T> mapper,
                                        final @NotNull String message) {
        if (!mapper.apply(t)) {
            throw new IllegalStateException(message);
        }
    }

    public static @NotNull String formatChannelId(final @NotNull String source,
                                                  final @NotNull String dest,
                                                  final @NotNull Namespace namespace) {
        return namespace.format(source + Hazelnut.PARTICIPANT_DELIMITER + dest);
    }

    public static void shutdownExecutor(final @NotNull ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1L, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (final InterruptedException ex) {
            executor.shutdownNow();
        }
    }
}
