package hazelnut.core.processor;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ProcessorRegistry {

    void register(final @NotNull MessageProcessor<?> processor);

    void unregister(final @NotNull MessageProcessor<?> processor);

    void unregisterAll(final @NotNull TypeToken<?> type);

    @NotNull Optional<MessageProcessor<?>> find(final @NotNull TypeToken<?> type);
}
