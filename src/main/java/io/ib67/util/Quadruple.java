package io.ib67.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * tuple of 4 elements
 * Also see: {@link Pair} {@link Triple}
 *
 * @since 1.0
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Quadruple<A, B, C, D> {
    public A A;
    public B B;
    public C C;
    public D D;

    public static <A, B, C, D> Quadruple<A, B, C, D> of(A a, B b, C c, D d) {
        return new Quadruple<>(a, b, c, d);
    }
}
