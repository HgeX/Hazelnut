package hazelnut.core;

import hazelnut.core.config.HazelnutConfig;
import hazelnut.core.processor.IncomingMessageListener;
import hazelnut.core.processor.ProcessorRegistry;
import hazelnut.core.processor.ResponseHandler;
import hazelnut.core.protocol.HeartbeatProcessor;
import hazelnut.core.protocol.HeartbeatTask;
import hazelnut.core.protocol.HeartbeatTranslator;
import hazelnut.core.translation.TranslatorRegistry;
import hazelnut.core.util.Miscellaneous;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class HazelnutImpl implements Hazelnut {
    private final ProcessorRegistry processors = ProcessorRegistry.create();
    private final TranslatorRegistry translators;
    private final MessageChannelFactory channelFactory;
    private final MessageBusFactory busFactory;
    private final String identity;
    private final Namespace namespace;
    private final ChannelLookup channelLookup;
    private final MessageAudience everyone;
    private final HeartbeatTask heartbeatTask;

    HazelnutImpl(final @NotNull String identity,
                 final @NotNull Namespace namespace,
                 final @NotNull MessageBusFactory busFactory,
                 final @NotNull HazelnutConfig config) {
        this.translators = TranslatorRegistry.create(config);
        this.identity = identity;
        this.namespace = namespace;
        final ResponseHandler responseHandler = new ResponseHandler(this, config);
        this.channelFactory = new MessageChannelFactoryImpl(
                namespace,
                busFactory,
                this.translators,
                new IncomingMessageListener(this.translators, responseHandler)
        );
        this.busFactory = busFactory;
        this.channelLookup = new ChannelLookupImpl(namespace, this.channelFactory, config);
        final MessageChannel.Duplex everyone = this.channelFactory.duplex(namespace.format(EVERYONE));
        this.channelLookup.registerStatic(everyone);
        this.everyone = audienceOf(Set.of(everyone));
        this.translators.add(new HeartbeatTranslator());
        this.processors.register(new HeartbeatProcessor(
                this.identity,
                (ChannelLookupImpl) this.channelLookup,
                namespace
        ));
        this.heartbeatTask = new HeartbeatTask(this, config, namespace);
        this.heartbeatTask.start();
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
        return audienceOf(this.channelLookup.volatileChannels().stream()
                .filter(x -> x instanceof MessageChannel.Outbound)
                .map(x -> (MessageChannel.Outbound) x)
                .collect(Collectors.toSet()));
    }

    @Override
    public @NotNull MessageAudience to(final @NotNull String name) throws IllegalArgumentException {
        final String actualId = Miscellaneous.formatChannelId(this.identity, name, this.namespace);
        return this.channelLookup.find(actualId)
                .filter(x -> x instanceof MessageChannel.Outbound)
                .map(x -> (MessageChannel.Outbound) x)
                .map(x -> audienceOf(Set.of(x)))
                .orElse(MessageAudience.EMPTY);
    }

    @Override
    public @NotNull ChannelLookup channelLookup() {
        return this.channelLookup;
    }

    @Override
    public @NotNull MessageChannelFactory channelFactory() {
        return this.channelFactory;
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
        this.heartbeatTask.close();
        final Set<MessageChannel> channels = Stream.of(this.channelLookup.staticChannels(), this.channelLookup.volatileChannels())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        for (final MessageChannel channel : channels) {
            channel.close();
        }

        this.channelLookup.close();
        this.busFactory.close();
    }

    private @NotNull MessageAudience audienceOf(final @NotNull Set<MessageChannel.Outbound> channels) {
        return channels.isEmpty()
                ? MessageAudience.EMPTY
                : new MessageAudienceImpl(channels, this.identity);
    }

    @Override
    public String toString() {
        return "Hazelnut{%s}".formatted(this.identity);
    }
}
