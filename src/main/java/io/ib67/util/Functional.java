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

    public static <T> Supplier<Consumer<T>> refEmptyConsumer() {
        return Functional::emptyConsumer;
    }

    public static <T> boolean outAndTrue(T t) {
        System.out.println(t);
        return true;
    }

    public static <T> T alsoMap(T t, Function<T, T> function) {
        return function.apply(t);
    }

    public static <T> T also(T t, Consumer<T> consumer) {
        return alsoMap(t, t2 -> {
            consumer.accept(t2);
            return t2;
        });
    }
}
