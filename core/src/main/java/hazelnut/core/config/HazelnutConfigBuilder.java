package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface HazelnutConfigBuilder {

    @NotNull HazelnutConfigBuilder policy(final @NotNull ClassNotFoundPolicy classNotFoundPolicy);

    @NotNull HazelnutConfigBuilder policy(final @NotNull MissingTranslatorPolicy missingTranslatorPolicy);

    @NotNull HazelnutConfigBuilder policy(final @NotNull NoProcessorPolicy noProcessorPolicy);

    @NotNull HazelnutConfigBuilder cacheExpiryRate(final long value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfigBuilder cacheHousekeeperRate(final long value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfigBuilder heartbeatRate(final long value, final @NotNull TimeUnit timeUnit);

    @NotNull HazelnutConfig build();
}
