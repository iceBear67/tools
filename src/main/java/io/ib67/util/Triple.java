package io.ib67.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * tuple of 4 elements
 * Also see: {@link Pair} {@link Quadruple}
 *
 * @since 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Triple<A, B, C> {
    public A A;
    public B B;
    public C C;

    public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
        return new Triple<>(a, b, c);
    }
}
