package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ClassNotFoundHandler {

    void classNotFound(final @NotNull String className);
}
