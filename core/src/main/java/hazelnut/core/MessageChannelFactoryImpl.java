package hazelnut.core;

import hazelnut.core.processor.IncomingMessageListener;
import hazelnut.core.translation.TranslationException;
import hazelnut.core.translation.TranslatorRegistry;
import hazelnut.core.util.NamedThreadFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import static hazelnut.core.util.Miscellaneous.logger;
import static java.util.Objects.requireNonNull;

final class MessageChannelFactoryImpl implements MessageChannelFactory {
    private static final String THREAD_NAME_PREFIX = "hazelnut-%s-bus-%s";
    private final Namespace namespace;
    private final MessageBusFactory busFactory;
    private final TranslatorRegistry translators;
    private final IncomingMessageListener messageListener;

    MessageChannelFactoryImpl(final @NotNull Namespace namespace,
                              final @NotNull MessageBusFactory busFactory,
                              final @NotNull TranslatorRegistry translators,
                              final @NotNull IncomingMessageListener messageListener) {
        this.namespace = requireNonNull(namespace, "namespace cannot be null");
        this.busFactory = requireNonNull(busFactory, "busFactory cannot be null");
        this.translators = requireNonNull(translators, "translators cannot be null");
        this.messageListener = requireNonNull(messageListener, "messageListener cannot be null");
    }

    @Override
    public @NotNull MessageChannel.Inbound inbound(final @NotNull String channelId) {
        final MessageBus messageBus = this.busFactory.create(channelId, executor(channelId));
        messageBus.addListener(this.messageListener::consume);
        return new InboundImpl(channelId, messageBus);
    }

    @Override
    public @NotNull MessageChannel.Outbound outbound(final @NotNull String channelId) {
        return new OutboundImpl(channelId, this.busFactory.create(channelId, executor(channelId)), this.translators);
    }

    @Override
    public @NotNull MessageChannel.Duplex duplex(final @NotNull String channelId) {
        final MessageBus messageBus = this.busFactory.create(channelId, executor(channelId));
        messageBus.addListener(this.messageListener::consume);
        return new DuplexImpl(channelId, messageBus, this.translators);
    }

    private @NotNull Executor executor(final @NotNull String channelId) {
        final String nameFormat = this.namespace.format(THREAD_NAME_PREFIX).formatted(this.busFactory.name(), channelId)
                + " #%d";
        final ThreadFactory threadFactory = new NamedThreadFactory(nameFormat);
        return Executors.newCachedThreadPool(threadFactory);
    }

    private static final class InboundImpl extends AbstractMessageChannel implements MessageChannel.Inbound {
        private InboundImpl(final @NotNull String channelId,
                            final @NotNull MessageBus messageBus) {
            super(channelId, messageBus);
        }
    }

    private static abstract class AbstractOutboundChannel extends AbstractMessageChannel implements MessageChannel.Outbound {
        private final TranslatorRegistry translators;
        private final Logger logger;

        private AbstractOutboundChannel(final @NotNull String channelId,
                                        final @NotNull MessageBus messageBus,
                                        final @NotNull TranslatorRegistry translators,
                                        final @NotNull Logger logger) {
            super(channelId, messageBus);
            this.translators = requireNonNull(translators, "translators cannot be null");
            this.logger = requireNonNull(logger, "logger cannot be null");
        }

        @Override
        public void send(final @NotNull HazelnutMessage<?> message) {
            try {
                final String finalMessage = this.translators.stringify(message);
                this.messageBus.deliver(finalMessage);

            } catch (final TranslationException ex) {
                this.logger.warning("Could not translate data to intermediary type");
                this.logger.warning(message.toString());
                ex.printStackTrace();
            }
        }
    }

    private static final class OutboundImpl extends AbstractOutboundChannel {
        private static final Logger LOGGER = logger(OutboundImpl.class);

        private OutboundImpl(final @NotNull String channelId,
                             final @NotNull MessageBus messageBus,
                             final @NotNull TranslatorRegistry translators) {
            super(channelId, messageBus, translators, LOGGER);
        }
    }

    private static final class DuplexImpl extends AbstractOutboundChannel implements MessageChannel.Duplex {
        private static final Logger LOGGER = logger(DuplexImpl.class);

        private DuplexImpl(final @NotNull String channelId,
                           final @NotNull MessageBus messageBus,
                           final @NotNull TranslatorRegistry translators) {
            super(channelId, messageBus, translators, LOGGER);
        }
    }
}
