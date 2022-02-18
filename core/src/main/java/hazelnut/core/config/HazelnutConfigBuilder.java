package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface HazelnutConfigBuilder {

    @NotNull HazelnutConfigBuilder policy(final @NotNull ClassNotFoundPolicy classNotFoundPolicy);

    @NotNull HazelnutConfigBuilder policy(final @NotNull MissingTranslatorPolicy missingTranslatorPolicy);

    @NotNull HazelnutConfigBuilder policy(final @NotNull NoProcessorPolicy noProcessorPolicy);

    @NotNull HazelnutConfigBuilder cacheExpiryRate(final int value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfigBuilder cacheHousekeeperRate(final int value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfigBuilder heartbeatRate(final int value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfig build();
}
