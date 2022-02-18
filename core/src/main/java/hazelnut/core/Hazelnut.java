package hazelnut.core;

import hazelnut.core.processor.ProcessorRegistry;
import hazelnut.core.translation.TranslatorRegistry;
import org.jetbrains.annotations.NotNull;

public interface Hazelnut extends AutoCloseable {
    String EVERYONE = "__everyone";
    String PARTICIPANT_DELIMITER = "->";

    @NotNull String identity();

    @NotNull MessageAudience everyone();

    @NotNull MessageAudience broadcast();

    @NotNull MessageAudience to(final @NotNull String name) throws IllegalArgumentException;

    @NotNull ChannelLookup channelLookup();

    @NotNull MessageChannelFactory channelFactory();

    @NotNull TranslatorRegistry translators();

    @NotNull ProcessorRegistry processors();

    static @NotNull HazelnutBuilder forIdentity(final @NotNull String identity) {
        return new HazelnutBuilderImpl(identity);
    }
}
