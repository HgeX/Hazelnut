package hazelnut.core.translation.builtin;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import hazelnut.core.processor.FailedResponse;
import hazelnut.core.processor.Response;
import hazelnut.core.processor.SuccessfulResponse;
import hazelnut.core.translation.MessageTranslator;
import hazelnut.core.translation.TranslationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResponseTranslator implements MessageTranslator<Response> {

    @Override
    public @NotNull Class<Response> type() {
        return Response.TYPE;
    }

    @Override
    public @NotNull JsonObject toIntermediary(final @NotNull Response object) throws TranslationException {
        final JsonObject result = new JsonObject()
                .add("status", object.status())
                .add("originalId", object.originalId().toString());
        if (object instanceof FailedResponse failed) {
            failed.errorMessage().ifPresent(x -> result.add("errorMessage", x));
            failed.errorClassName().ifPresent(x -> result.add("errorClassName", x));
        }

        return result;
    }

    @Override
    public @NotNull Response fromIntermediary(final @NotNull JsonObject intermediary) throws TranslationException {
        final boolean status = intermediary.get("status").asBoolean();
        final String originalIdString = intermediary.get("originalId").asString();
        final UUID originalId;
        try {
            originalId = UUID.fromString(originalIdString);
        } catch (final IllegalArgumentException ex) {
            throw new TranslationException("Got invalid UUID: %s".formatted(originalIdString));
        }

        if (status) {
            return new SuccessfulResponse(originalId);
        } else {
            final @Nullable String errorClassName = jsonOrNull(() -> intermediary.get("errorClassName"), JsonValue::asString);
            final @Nullable String errorMessage = jsonOrNull(() -> intermediary.get("errorMessage"), JsonValue::asString);

            return new FailedResponse(originalId, errorClassName, errorMessage);
        }
    }

    private static <T> @Nullable T jsonOrNull(final @NotNull Supplier<JsonValue> valueSupplier,
                                              final @NotNull Function<JsonValue, T> mapper) {
        final JsonValue value = valueSupplier.get();
        return value != null ? mapper.apply(value) : null;
    }
}
