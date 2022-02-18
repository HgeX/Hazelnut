package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public enum ClassNotFoundPolicy {
    IGNORE(className -> {}),
    FAIL(className -> {
        throw new RuntimeException("Unable to load class %s".formatted(className));
    });

    private final ClassNotFoundHandler handler;

    ClassNotFoundPolicy(final @NotNull ClassNotFoundHandler handler) {
        this.handler = requireNonNull(handler, "handler cannot be null");
    }

    public @NotNull ClassNotFoundHandler handler() {
        return this.handler;
    }
}
