package hazelnut.core;

import hazelnut.core.translate.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

public interface Hazelnut extends AutoCloseable {

    @NotNull String identity();

    @NotNull MessageAudience everyone();

    @NotNull MessageAudience broadcast();

    @NotNull MessageAudience to(final @NotNull String name) throws IllegalArgumentException;

    @NotNull ChannelLookup channelLookup();

    @NotNull TranslatorRegistry translators();

    static @NotNull HazelnutBuilder forIdentity(final @NotNull String identity) {
        return new HazelnutBuilderImpl(identity);
    }
}
