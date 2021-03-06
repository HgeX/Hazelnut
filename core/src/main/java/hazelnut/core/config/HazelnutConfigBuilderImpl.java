package hazelnut.core.config;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

final class HazelnutConfigBuilderImpl implements HazelnutConfigBuilder {
    private ClassNotFoundPolicy classNotFoundPolicy;
    private MissingTranslatorPolicy missingTranslatorPolicy;
    private NoProcessorPolicy noProcessorPolicy;
    private long cacheExpiryRate;
    private long cacheHousekeeperRate;
    private long heartbeatRate;

    @Override
    public @NotNull HazelnutConfigBuilder policy(final @NotNull ClassNotFoundPolicy classNotFoundPolicy) {
        this.classNotFoundPolicy = classNotFoundPolicy;
        return this;
    }

    @Override
    public @NotNull HazelnutConfigBuilder policy(final @NotNull MissingTranslatorPolicy missingTranslatorPolicy) {
        this.missingTranslatorPolicy = missingTranslatorPolicy;
        return this;
    }

    @Override
    public @NotNull HazelnutConfigBuilder policy(final @NotNull NoProcessorPolicy noProcessorPolicy) {
        this.noProcessorPolicy = noProcessorPolicy;
        return this;
    }

    @Override
    public @NotNull HazelnutConfigBuilder cacheExpiryRate(long value, @NotNull TimeUnit timeUnit) {
        requireNonNull(timeUnit, "timeUnit cannot be null");
        this.cacheExpiryRate = timeUnit.toMillis(value);
        return this;
    }

    @Override
    public @NotNull HazelnutConfigBuilder cacheHousekeeperRate(final long value, final @NotNull TimeUnit timeUnit) {
        requireNonNull(timeUnit, "timeUnit cannot be null");
        this.cacheHousekeeperRate = timeUnit.toMillis(value);
        return this;
    }

    @Override
    public @NotNull HazelnutConfigBuilder heartbeatRate(final long value, final @NotNull TimeUnit timeUnit) {
        requireNonNull(timeUnit, "timeUnit cannot be null");
        this.heartbeatRate = timeUnit.toMillis(value);
        return this;
    }

    @Override
    public @NotNull HazelnutConfig build() {
        return new HazelnutConfigImpl(
                this.classNotFoundPolicy,
                this.missingTranslatorPolicy,
                this.noProcessorPolicy,
                this.cacheExpiryRate,
                this.cacheHousekeeperRate,
                this.heartbeatRate
        );
    }
}
