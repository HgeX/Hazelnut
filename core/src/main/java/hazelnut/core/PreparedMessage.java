package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public record PreparedMessage<T>(@NotNull MessageHeader header, @NotNull Message<T> data) {}
