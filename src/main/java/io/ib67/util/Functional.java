package io.ib67.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Functional {
    private Functional() {

    }

    public static <T> T from(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T, R> R from(T param, Function<T, R> supplier) {
        return supplier.apply(param);
    }

    private static final Consumer<?> EMPTY_CONSUMER = t -> {
    };

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}
