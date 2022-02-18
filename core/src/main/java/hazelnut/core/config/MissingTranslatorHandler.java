package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MissingTranslatorHandler {

    void missingTranslator(final @NotNull Class<?> type);
}
