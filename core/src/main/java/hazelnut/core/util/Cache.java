package hazelnut.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.requireNonNull;

public final class Cache<K, V> {
    private final Map<K, Node<V>> entries = new ConcurrentHashMap<>();
    private final long lifetime;
    private final Consumer<V> evictionListener;

    private Cache(final long lifetime, final @NotNull Consumer<V> evictionListener) {
        this.lifetime = lifetime;
        this.evictionListener = evictionListener;
    }

    public @NotNull Optional<V> findByKey(final @NotNull K key) {
        requireNonNull(key, "key cannot be null");
        final @Nullable Node<V> found = this.entries.get(key);
        if (found == null) {
            return Optional.empty();
        }

        if (removeIfExpired(key, found)) {
            return Optional.empty();
        }

        return Optional.of(found.value());
    }

    public void cache(final @NotNull K key, final @NotNull V value) {
        requireNonNull(key, "key cannot be null");
        requireNonNull(value, "value cannot be null");
        final Node<V> node = new Node<>(value, currentTimeMillis());
        this.entries.put(key, node);
    }

    public void rebirth(final @NotNull K key) {
        requireNonNull(key, "key cannot be null");
        final @Nullable Node<V> existing = this.entries.get(key);
        if (existing == null) {
            throw new IllegalArgumentException("No value present for key %s".formatted(key));
        }

        cache(key, existing.value());
    }

    public @NotNull Set<V> values() {
        final Set<V> result = new HashSet<>();
        for (final Map.Entry<K, Node<V>> entry : this.entries.entrySet()) {
            if (!removeIfExpired(entry.getKey(), entry.getValue())) {
                result.add(entry.getValue().value());
            }
        }

        return result;
    }

    // Return true if the node has expired
    private boolean checkExpiry(final @NotNull Node<V> node) {
        return node.birth() + this.lifetime < currentTimeMillis();
    }

    // Return true if the node expired
    private boolean removeIfExpired(final @NotNull K key, final @NotNull Node<V> node) {
        if (checkExpiry(node)) {
            this.entries.remove(key);
            this.evictionListener.accept(node.value());

            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return this.entries.toString();
    }

    private static final record Node<V>(@NotNull V value, long birth) {}

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static final class Builder<K, V> {
        private Duration lifetime;
        private Consumer<V> evictionListener;

        private Builder() {}

        public @NotNull Builder<K, V> lifetime(final @NotNull Duration lifetime) {
            this.lifetime = lifetime;
            return this;
        }

        public @NotNull Builder<K, V> evictionListener(final @NotNull Consumer<V> evictionListener) {
            this.evictionListener = evictionListener;
            return this;
        }

        public @NotNull Cache<K, V> build() {
            requireNonNull(this.lifetime, "lifetime cannot be null");
            final Consumer<V> evictionListener = this.evictionListener != null
                    ? this.evictionListener
                    : x -> {};

            return new Cache<>(this.lifetime.toMillis(), evictionListener);
        }
    }
}
