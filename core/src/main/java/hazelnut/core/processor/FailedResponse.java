package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

final class FailedResponse extends AbstractResponse {
    private final String errorClassName;
    private final String errorMessage;

    FailedResponse(final @NotNull UUID originalId,
                   final @Nullable String errorClassName,
                   final @Nullable String errorMessage) {
        super(originalId, false);
        this.errorClassName = errorClassName;
        this.errorMessage = errorMessage;
    }

    public @NotNull Optional<String> errorClassName() {
        return Optional.ofNullable(this.errorClassName);
    }

    public @NotNull Optional<String> errorMessage() {
        return Optional.ofNullable(this.errorMessage);
    }
}
