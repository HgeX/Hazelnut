package hazelnut.core.translate;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface TranslatorCollection {

    void add(final @NotNull MessageTranslator<?, ?> translator);

    void replace(final @NotNull MessageTranslator<?, ?> translator);

    void remove(final @NotNull MessageTranslator<?, ?> translator);

    @NotNull Set<MessageTranslator<?, ?>> entries();

    @NotNull <T, I> Optional<MessageTranslator<T, I>> find(final @NotNull TypeToken<T> type);
}
