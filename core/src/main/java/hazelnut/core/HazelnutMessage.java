package hazelnut.core;

import org.jetbrains.annotations.NotNull;

public record HazelnutMessage<T extends Message<T>>(@NotNull MessageHeader header, @NotNull T data) {}
