package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

abstract class AbstractResponse implements Response {
    private final UUID originalId;
    private final boolean status;

    AbstractResponse(final @NotNull UUID originalId,
                     final boolean status) {
        this.originalId = requireNonNull(originalId);
        this.status = status;
    }

    @Override
    public final @NotNull UUID originalId() {
        return this.originalId;
    }

    @Override
    public final boolean status() {
        return this.status;
    }
}
