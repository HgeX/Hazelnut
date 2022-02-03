package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface MessageBusFactory {

    @NotNull MessageBus create(final @NotNull String name);
}
