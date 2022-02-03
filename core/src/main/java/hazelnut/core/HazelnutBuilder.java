package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import hazelnut.core.translate.TranslatorCollection;

import java.util.concurrent.Executor;

public interface HazelnutBuilder {

    @NotNull HazelnutBuilder namespace(final @NotNull Namespace namespace);

    default @NotNull HazelnutBuilder namespace(final @NotNull String namespace) {
        return namespace(Namespace.of(namespace));
    }

    @NotNull HazelnutBuilder translators(final @NotNull TranslatorCollection translators);

    @NotNull HazelnutBuilder executor(final @NotNull Executor executor);

    @NotNull HazelnutBuilder busFactory(final @NotNull MessageBusFactory busFactory);

    @NotNull Hazelnut build();
}
