package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public final class ProcessorRegistryImpl implements ProcessorRegistry {
    private final Map<TypeToken<?>, List<MessageProcessor<?>>> processors = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void register(final @NotNull MessageProcessor<?> processor) {
        try {
            this.lock.lock();
            final TypeToken<?> type = processor.messageType();
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
            final TypeToken<?> type = processor.messageType();
            final List<MessageProcessor<?>> processors = this.processors.get(type);
            if (processors != null) {
                processors.remove(processor);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void unregisterAll(final @NotNull TypeToken<?> type) {
        try {
            this.lock.lock();
            this.processors.remove(type);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public @NotNull Optional<MessageProcessor<?>> find(final @NotNull TypeToken<?> type) {
        try {
            this.lock.lock();
            final List<MessageProcessor<?>> processors = this.processors.get(type);
            if (processors == null) {
                return Optional.empty();
            }

            throw new UnsupportedOperationException();
        } finally {
            this.lock.unlock();
        }
    }
}