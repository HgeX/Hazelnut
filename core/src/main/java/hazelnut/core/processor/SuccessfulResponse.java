package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class SuccessfulResponse extends AbstractResponse {

    public SuccessfulResponse(final @NotNull UUID originalId) {
        super(originalId, true);
    }
}
