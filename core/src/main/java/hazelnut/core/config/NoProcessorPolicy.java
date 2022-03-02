package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public enum NoProcessorPolicy {
    IGNORE(type -> {}),
    WARN(type -> NoProcessorHandler.LOGGER.warning("No processors present for type %s".formatted(type)));

    private final NoProcessorHandler handler;

    NoProcessorPolicy(final @NotNull NoProcessorHandler handler) {
        this.handler = requireNonNull(handler, "handler cannot be null");
    }

    public @NotNull NoProcessorHandler handler() {
        return this.handler;
    }
}
