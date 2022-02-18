package hazelnut.core;

import hazelnut.core.config.HazelnutConfig;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

final class HazelnutBuilderImpl implements HazelnutBuilder {
    private final String identity;
    private Namespace namespace;
    private MessageBusFactory busFactory;
    private HazelnutConfig config;

    HazelnutBuilderImpl(final @NotNull String identity) {
        this.identity = requireNonNull(identity, "identity cannot be null");
    }

    @Override
    public @NotNull HazelnutBuilder namespace(final @NotNull Namespace namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public @NotNull HazelnutBuilder busFactory(final @NotNull MessageBusFactory busFactory) {
        this.busFactory = busFactory;
        return this;
    }

    @Override
    public @NotNull HazelnutBuilder config(final @NotNull HazelnutConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public @NotNull Hazelnut build() {
        requireNonNull(this.namespace, "namespace cannot be null");
        requireNonNull(this.busFactory, "busFactory cannot be null");
        requireNonNull(this.config, "config cannot be null");

        return new HazelnutImpl(
                this.identity,
                this.namespace,
                this.busFactory,
                this.config
        );
    }
}
