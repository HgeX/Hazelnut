package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface MessageHeader {

    static @NotNull MessageHeader of(final @NotNull UUID messageId,
                                     final @NotNull String originId,
                                     final @NotNull Class<?> type) {
        return new MessageHeaderImpl(messageId, originId, type);
    }

    @NotNull UUID messageId();

    @NotNull String originId();

    @NotNull Class<?> type();
}
