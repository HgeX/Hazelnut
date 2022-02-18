package hazelnut.core;

import hazelnut.core.config.HazelnutConfig;
import org.jetbrains.annotations.NotNull;

public interface HazelnutBuilder {

    @NotNull HazelnutBuilder namespace(final @NotNull Namespace namespace);

    default @NotNull HazelnutBuilder namespace(final @NotNull String namespace) {
        return namespace(Namespace.of(namespace));
    }

    @NotNull HazelnutBuilder busFactory(final @NotNull MessageBusFactory busFactory);

    @NotNull HazelnutBuilder config(final @NotNull HazelnutConfig config);

    @NotNull Hazelnut build();
}
