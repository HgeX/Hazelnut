package hazelnut.core;

import hazelnut.core.processor.ProcessorRegistry;
import hazelnut.core.processor.ProcessorRegistryImpl;
import hazelnut.core.translation.TranslatorRegistry;
import hazelnut.core.translation.TranslatorRegistryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

final class HazelnutImpl implements Hazelnut {
    private static final String EVERYONE = "__everyone";
    private static final String PARTICIPANT_DELIMITER = "->";
    private final TranslatorRegistry translators = new TranslatorRegistryImpl();
    private final ProcessorRegistry processors = new ProcessorRegistryImpl();
    private final String identity;
    private final Namespace namespace;
    private final Executor executor;
    private final ChannelLookup channelLookup;
    private final MessageAudience everyone;

    HazelnutImpl(final @NotNull String identity,
                 final @NotNull Namespace namespace,
                 final @NotNull Executor executor,
                 final @NotNull MessageBusFactory busFactory) {
        this.identity = identity;
        this.namespace = namespace;
        this.executor = executor;
        final MessageChannelFactory channelFactory = new MessageChannelFactory(busFactory, this.translators);
        this.channelLookup = new ChannelLookupImpl(namespace, channelFactory);
        final MessageChannel everyone = channelFactory.createChannelWithId(EVERYONE);
        this.channelLookup.register(everyone);
        this.everyone = audienceOf(Set.of(everyone));
    }

    @Override
    public @NotNull String identity() {
        return this.identity;
    }

    @Override
    public @NotNull MessageAudience everyone() {
        return this.everyone;
    }

    @Override
    public @NotNull MessageAudience broadcast() {
        // Send the data to everyone, but us. To achieve this,
        // the only thing we need to do, is grab every known channel
        // (except the __everyone) and create an audience with them.
        final String namespacedEveryone = this.namespace.format(EVERYONE);
        final Set<MessageChannel> channels = this.channelLookup
                .channels()
                .stream()
                .filter(x -> !x.channelId().startsWith(namespacedEveryone))
                .collect(Collectors.toSet());
        return audienceOf(channels);
    }

    @Override
    public @NotNull MessageAudience to(final @NotNull String name) throws IllegalArgumentException {
        final String actualId = this.identity + PARTICIPANT_DELIMITER + name;
        return this.channelLookup.find(actualId)
                .map(x -> audienceOf(Set.of(x)))
                .orElse(MessageAudience.EMPTY);
    }

    @Override
    public @NotNull ChannelLookup channelLookup() {
        return this.channelLookup;
    }

    @Override
    public @NotNull TranslatorRegistry translators() {
        return this.translators;
    }

    @Override
    public @NotNull ProcessorRegistry processors() {
        return this.processors;
    }

    @Override
    public void close() throws Exception {
        final Set<MessageChannel> channels = this.channelLookup.channels();
        for (final MessageChannel channel : channels) {
            channel.close();
        }
    }

    private @NotNull MessageAudience audienceOf(final @NotNull Set<MessageChannel> channels) {
        return new MessageAudienceImpl(this.executor, channels, this.identity);
    }

    @Override
    public String toString() {
        return "Hazelnut[%s]".formatted(this.identity);
    }
}
