package hazelnut.core.util;

import hazelnut.core.Namespace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
