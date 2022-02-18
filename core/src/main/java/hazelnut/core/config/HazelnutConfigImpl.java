package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import static hazelnut.core.util.Miscellaneous.requireState;
import static java.util.Objects.requireNonNull;

record HazelnutConfigImpl(@NotNull ClassNotFoundPolicy classNotFoundPolicy,
                          @NotNull MissingTranslatorPolicy missingTranslatorPolicy,
                          @NotNull NoProcessorPolicy noProcessorPolicy,
                          long cacheExpiryRate,
                          long cacheHouskeeperRate,
                          long heartbeatRate) implements HazelnutConfig {

    HazelnutConfigImpl {
        requireNonNull(classNotFoundPolicy, "classNotFoundPolicy cannot be null");
        requireNonNull(missingTranslatorPolicy, "missingTranslatorPolicy cannot be null");
        requireNonNull(noProcessorPolicy, "noProcessorPolicy cannot be null");
        requireState(cacheExpiryRate, x -> x > 0, "cacheExpiryRate cannot be less than 1");
        requireState(cacheHouskeeperRate,  x -> x > 0, "cacheHousekeeperRate cannot be less than 1");
        requireState(heartbeatRate, x -> x > 0, "heartbeatRate cannot be less than 1");
    }
}
