package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

final class MessageHeaderImpl implements MessageHeader {
    private final UUID messageId;
    private final String identity;
    private final Class<?> type;

    MessageHeaderImpl(final @NotNull UUID messageId,
                      final @NotNull String identity,
                      final @NotNull Class<?> type) {
        this.messageId = requireNonNull(messageId, "messageId cannot be null");
        this.identity = requireNonNull(identity, "identity cannot be null");
        this.type = requireNonNull(type, "type cannot be null");
    }

    MessageHeaderImpl(final @NotNull String identity,
                             final @NotNull Class<?> type) {
        this(UUID.randomUUID(), identity, type);
    }

    @Override
    public @NotNull UUID messageId() {
        return this.messageId;
    }

    @Override
    public @NotNull String originId() {
        return this.identity;
    }

    @Override
    public @NotNull Class<?> type() {
        return this.type;
    }

    @Override
    public String toString() {
        return "MessageHeaderImpl{messageId=%s, originId=%s, type=%s}".formatted(
                this.messageId, this.identity, this.type.getName());
    }
}
