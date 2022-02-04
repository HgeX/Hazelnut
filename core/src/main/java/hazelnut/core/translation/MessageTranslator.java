package hazelnut.core.translation;

import com.eclipsesource.json.JsonObject;
import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

public interface MessageTranslator<T> {

    @NotNull TypeToken<T> type();

    @NotNull JsonObject toIntermediary(final @NotNull T object) throws TranslationException;

    @NotNull T fromIntermediary(final @NotNull JsonObject intermediary) throws TranslationException;
}
