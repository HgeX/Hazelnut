package hazelnut.core.translation;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

public class TranslationException extends Exception {
    @Serial
    private static final long serialVersionUID = -7771058559977335899L;

    public TranslationException(final @NotNull String message) {
        super(message);
    }
}
