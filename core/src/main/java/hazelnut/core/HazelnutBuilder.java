package hazelnut.core;

import hazelnut.core.config.HazelnutConfig;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public interface HazelnutBuilder {

    @NotNull HazelnutBuilder namespace(final @NotNull Namespace namespace);

    default @NotNull HazelnutBuilder namespace(final @NotNull String namespace) {
        return namespace(Namespace.of(namespace));
    }

    @NotNull HazelnutBuilder executor(final @NotNull Executor executor);

    @NotNull HazelnutBuilder busFactory(final @NotNull MessageBusFactory busFactory);

    @NotNull HazelnutBuilder config(final @NotNull HazelnutConfig config);

    @NotNull Hazelnut build();
}
