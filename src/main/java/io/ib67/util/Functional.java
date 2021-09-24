package io.ib67.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class Functional {
    private Functional(){

    }
    public static <T> T from(Supplier<T> supplier){
        return supplier.get();
    }
    public static <T,R> R from(T param,Function<T,R> supplier){
        return supplier.apply(param);
    }
}
