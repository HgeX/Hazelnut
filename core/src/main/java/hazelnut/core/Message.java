package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public interface Message<T> {

    @NotNull Class<T> type();
}
