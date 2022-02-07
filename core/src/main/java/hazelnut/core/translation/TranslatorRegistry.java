package hazelnut.core.translation;

import com.google.common.reflect.TypeToken;
import hazelnut.core.HazelnutMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface TranslatorRegistry {

    void add(final @NotNull MessageTranslator<?> translator);

    boolean replace(final @NotNull MessageTranslator<?> translator);

    void remove(final @NotNull MessageTranslator<?> translator);

    @NotNull Set<MessageTranslator<?>> entries();

    @NotNull <T> Optional<MessageTranslator<T>> find(final @NotNull TypeToken<T> type);

    @NotNull String stringify(final @NotNull HazelnutMessage<?> message) throws TranslationException;
}
