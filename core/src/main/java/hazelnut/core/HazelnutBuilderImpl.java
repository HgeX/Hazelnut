package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import hazelnut.core.translate.TranslatorCollection;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

final class HazelnutBuilderImpl implements HazelnutBuilder {
    private final String identity;
    private Namespace namespace;
    private TranslatorCollection translators;
    private Executor executor;
    private MessageBusFactory busFactory;

    HazelnutBuilderImpl(final @NotNull String identity) {
        this.identity = requireNonNull(identity, "identity cannot be null");
    }

    @Override
    public @NotNull HazelnutBuilder namespace(final @NotNull Namespace namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public @NotNull HazelnutBuilder translators(final @NotNull TranslatorCollection translators) {
        this.translators = translators;
        return this;
    }

    @Override
    public @NotNull HazelnutBuilder executor(final @NotNull Executor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public @NotNull HazelnutBuilder busFactory(final @NotNull MessageBusFactory busFactory) {
        this.busFactory = busFactory;
        return this;
    }

    @Override
    public @NotNull Hazelnut build() {
        requireNonNull(this.namespace, "namespace cannot be null");
        requireNonNull(this.busFactory, "busFactory cannot be null");
        requireNonNull(this.translators, "translators cannot be null");

        final Executor executor = this.executor == null ? Executors.newCachedThreadPool() : this.executor;
        return new HazelnutImpl(
                this.identity,
                this.namespace,
                this.translators,
                executor,
                this.busFactory
        );
    }
}
