package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

final class MessageHeaderImpl implements MessageHeader {
    private final UUID messageId = UUID.randomUUID();
    private final String identity;

    MessageHeaderImpl(final @NotNull String identity) {
        this.identity = requireNonNull(identity, "identity cannot be null");
    }

    @Override
    public @NotNull UUID messageId() {
        return this.messageId;
    }

    @Override
    public @NotNull String originId() {
        return this.identity;
    }
}
