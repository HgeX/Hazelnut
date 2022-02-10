package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public final class ProcessorRegistryImpl implements ProcessorRegistry {
    private final Map<Class<?>, List<MessageProcessor<?>>> processors = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public ProcessorRegistryImpl() {
        register(new ResponseProcessor());
    }

    @Override
    public void register(final @NotNull MessageProcessor<?> processor) {
        try {
            this.lock.lock();
            final Class<?> type = processor.messageType();
            final List<MessageProcessor<?>> processors = this.processors.computeIfAbsent(type, x -> new ArrayList<>());
            processors.add(processor);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void unregister(final @NotNull MessageProcessor<?> processor) {
        try {
            this.lock.lock();
            final Class<?> type = processor.messageType();
            final List<MessageProcessor<?>> processors = this.processors.get(type);
            if (processors != null) {
                processors.remove(processor);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void unregisterAll(final @NotNull Class<?> type) {
        try {
            this.lock.lock();
            this.processors.remove(type);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull List<MessageProcessor<?>> find(final @NotNull Class<?> type) {
        try {
            this.lock.lock();
            final List<MessageProcessor<?>> processors = this.processors.get(type);
            if (processors == null) {
                return List.of();
            }

            return List.copyOf(processors);
        } finally {
            this.lock.unlock();
        }
    }
}
