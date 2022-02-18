package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

public interface HazelnutConfig {
    long DEFAULT_CACHE_EXPIRY_RATE = 15000; // 15 seconds
    long DEFAULT_CACHE_HOUSEKEEPER_RATE = 30000; // 30 seconds
    long DEFAULT_HEARTBEAT_RATE = 5000; // 5 seconds

    @NotNull ClassNotFoundPolicy classNotFoundPolicy();

    @NotNull MissingTranslatorPolicy missingTranslatorPolicy();

    @NotNull NoProcessorPolicy noProcessorPolicy();

    long cacheExpiryRate();

    long cacheHouskeeperRate();

    long heartbeatRate();

    static @NotNull HazelnutConfigBuilder builder() {
        return new HazelnutConfigBuilderImpl();
    }

    static @NotNull HazelnutConfig defaultConfig() {
        return new HazelnutConfigImpl(
                ClassNotFoundPolicy.FAIL,
                MissingTranslatorPolicy.FAIL,
                NoProcessorPolicy.WARN,
                HazelnutConfig.DEFAULT_CACHE_EXPIRY_RATE,
                HazelnutConfig.DEFAULT_CACHE_HOUSEKEEPER_RATE,
                HazelnutConfig.DEFAULT_HEARTBEAT_RATE
        );
    }
}
