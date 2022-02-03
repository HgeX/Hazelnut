package hazelnut.core;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public interface Namespace {
    String DELIMITER = ":";
    Pattern VALID_NAME = Pattern.compile("[\\w\\-]+");

    @NotNull String namespace();

    @NotNull String format(final @NotNull String value);

    static @NotNull Namespace of(final @NotNull String namespace) {
        return new NamespaceImpl(namespace);
    }
}
