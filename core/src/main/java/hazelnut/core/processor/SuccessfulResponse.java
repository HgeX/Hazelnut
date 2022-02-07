package hazelnut.core.processor;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

final class SuccessfulResponse extends AbstractResponse {

    SuccessfulResponse(final @NotNull UUID originalId) {
        super(originalId, true);
    }
}
