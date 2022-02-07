package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public record PreparedMessage<T extends Message<T>>(@NotNull MessageHeader header, @NotNull T data) {}
