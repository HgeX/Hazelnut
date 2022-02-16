package hazelnut.core.translation;

import hazelnut.core.HazelnutMessage;
import hazelnut.core.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface TranslatorRegistry {

    static @NotNull TranslatorRegistry create() {
        return new TranslatorRegistryImpl();
    }

    void add(final @NotNull MessageTranslator<?> translator);

    boolean replace(final @NotNull MessageTranslator<?> translator);

    void remove(final @NotNull MessageTranslator<?> translator);

    @NotNull Set<MessageTranslator<?>> entries();

    @NotNull <T> Optional<MessageTranslator<T>> find(final @NotNull Class<T> type);

    @NotNull String stringify(final @NotNull HazelnutMessage<?> message) throws TranslationException;

    @NotNull <T extends Message<T>> HazelnutMessage<T> parse(final @NotNull String message) throws TranslationException;
}
