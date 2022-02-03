package hazelnut.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

import static java.util.Objects.requireNonNull;

final class NamespaceImpl implements Namespace {
    private final String namespace;

    NamespaceImpl(final @NotNull String namespace) {
        requireNonNull(namespace, "namespace cannot be null");
        final Matcher matcher = VALID_NAME.matcher(namespace);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Illegal namespace (%s). Must match %s".formatted(namespace, VALID_NAME.pattern()));
        }

        this.namespace = namespace;
    }

    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

    @Override
    public @NotNull String format(final @NotNull String value) {
        return this.namespace + DELIMITER + value;
    }

    @Override
    public String toString() {
        return "Namespace[%s]".formatted(this.namespace);
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Namespace namespace1 = (Namespace) o;
        return Objects.equals(this.namespace, namespace1.namespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace);
    }
}
