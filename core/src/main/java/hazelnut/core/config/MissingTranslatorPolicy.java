package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public enum MissingTranslatorPolicy {
    IGNORE(type -> {}),
    FAIL(type -> {
        throw new IllegalStateException("Could not find message translator for type %s".formatted(type.getName()));
    });

    private final MissingTranslatorHandler handler;

    MissingTranslatorPolicy(final @NotNull MissingTranslatorHandler handler) {
        this.handler = requireNonNull(handler, "handler cannot be null");
    }

    public @NotNull MissingTranslatorHandler handler() {
        return this.handler;
    }
}
