package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;

@FunctionalInterface
public interface NoProcessorHandler {
    Logger LOGGER = logger(NoProcessorHandler.class);

    void noProcessors(final @NotNull Class<?> type);
}
