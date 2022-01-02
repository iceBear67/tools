package io.ib67.util;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class CatchingContext<T> {
    private Throwable failure;
    private T result;

    public CatchingContext(T result) {
        this.result = result;
    }

    public CatchingContext(Throwable failure) {
        this.failure = failure;
    }

    public CatchingContext<T> alsoPrintStack() {
        if (failure != null) {
            failure.printStackTrace();
        }
        return this;
    }

    public CatchingContext<T> onFailure(Consumer<Throwable> consumer) {
        return onFailure((t, r) -> {
            consumer.accept(failure);
            return r;
        });
    }

    public CatchingContext<T> onFailure(BiFunction<Throwable, T, T> mapper) {
        if (failure != null) {
            result = mapper.apply(failure, result);
        }
        return this;
    }

    public boolean isFailed() {
        return failure != null;
    }

    public T getResult() {
        return result;
    }

    public CatchingContext<T> onSuccess(Consumer<T> consumer) {
        return onSuccess((r) -> {
            consumer.accept(r);
            return r;
        });
    }

    public CatchingContext<T> onSuccess(Function<T, T> consumer) {
        if (result != null) {
            result = consumer.apply(result);
        }
        return this;
    }
}
