package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import hazelnut.core.translate.TranslatorCollection;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

final class HazelnutImpl implements Hazelnut {
    private static final String EVERYONE = "__everyone";
    private static final String PARTICIPANT_DELIMITER = "->";
    private final String identity;
    private final Namespace namespace;
    private final Executor executor;
    private final ChannelLookup channelLookup;
    private final MessageAudience everyone;

    HazelnutImpl(final @NotNull String identity,
                 final @NotNull Namespace namespace,
                 final @NotNull TranslatorCollection translators,
                 final @NotNull Executor executor,
                 final @NotNull MessageBusFactory busFactory) {
        this.identity = identity;
        this.namespace = namespace;
        this.executor = executor;
        final MessageChannelFactory channelFactory = new MessageChannelFactory(busFactory, translators);
        this.channelLookup = new ChannelLookupImpl(namespace, channelFactory);
        final MessageChannel everyone = channelFactory.createChannelWithId(EVERYONE);
        this.channelLookup.register(everyone);
        this.everyone = new MessageAudienceImpl(this.executor, Set.of(everyone));
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
        // Send the message to everyone, but us. To achieve this,
        // the only thing we need to do, is grab every known channel
        // (except the __everyone) and create an audience with them.
        final String namespacedEveryone = this.namespace.format(EVERYONE);
        final Set<MessageChannel> channels = this.channelLookup
                .channels()
                .stream()
                .filter(x -> !x.channelId().startsWith(namespacedEveryone))
                .collect(Collectors.toSet());
        return new MessageAudienceImpl(this.executor, channels);
    }

    @Override
    public @NotNull MessageAudience to(final @NotNull String name) throws IllegalArgumentException {
        final String actualId = this.identity + PARTICIPANT_DELIMITER + name;
        return this.channelLookup.find(actualId)
                .map(x -> new MessageAudienceImpl(this.executor, Set.of(x)))
                .map(MessageAudience.class::cast) // I hate you Java
                .orElse(MessageAudience.EMPTY);
    }

    @Override
    public @NotNull ChannelLookup channelLookup() {
        return this.channelLookup;
    }

    @Override
    public void close() throws Exception {
        final Set<MessageChannel> channels = this.channelLookup.channels();
        for (final MessageChannel channel : channels) {
            channel.close();
        }
    }
}
