package hazelnut.core.translate;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

public interface MessageTranslator<T, I> {

    @NotNull TypeToken<T> type();

    @NotNull I toIntermediary(final @NotNull T object) throws TranslationException;

    @NotNull T fromIntermediary(final @NotNull I intermediary) throws TranslationException;
}
