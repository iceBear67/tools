package io.ib67.util;

import lombok.EqualsAndHashCode;

/**
 * tuple
 * Also see: {@link Quadruple} {@link Triple}
 *
 * @since 1.0
 */
@EqualsAndHashCode
public class Pair<K, V> {
    public K key;
    public V value;

    protected Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Pair<K, V> of(K a, V b) {
        return new Pair<>(a, b);
    }
}
