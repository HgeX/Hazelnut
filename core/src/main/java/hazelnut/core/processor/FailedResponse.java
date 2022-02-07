package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

final class FailedResponse implements Response {
    private final String errorClassName;
    private final String errorMessage;

    FailedResponse(final @Nullable String errorClassName,
                   final @Nullable String errorMessage) {
        this.errorClassName = errorClassName;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean status() {
        return false;
    }

    public @NotNull Optional<String> errorClassName() {
        return Optional.ofNullable(this.errorClassName);
    }

    public @NotNull Optional<String> errorMessage() {
        return Optional.ofNullable(this.errorMessage);
    }
}
