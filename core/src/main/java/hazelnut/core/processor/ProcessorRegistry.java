package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ProcessorRegistry {

    static @NotNull ProcessorRegistry create() {
        return new ProcessorRegistryImpl();
    }

    void register(final @NotNull MessageProcessor<?> processor);

    void unregister(final @NotNull MessageProcessor<?> processor);

    void unregisterAll(final @NotNull Class<?> type);

    @NotNull List<MessageProcessor<?>> find(final @NotNull Class<?> type);
}
