package io.ib67.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class Lazy<T,R> {
    private Function<T,R> supplier;
    private R value;
    private final byte[] lock = new byte[0];
    private Lazy(Function<T,R> supplier){
        this.supplier = supplier;
    }
    public static <T,R> Lazy<T,R> by(Supplier<R> supplier){
        return new Lazy<>(t -> supplier.get());
    }
    public static <T,R> Lazy<T,R> by(Function<T,R> supplier){
        return new Lazy<>(supplier);
    }
    public R getLocked(T param) {
        synchronized (lock) {
            if (value == null) {
                value = supplier.apply(param);
                supplier = null;
            }
            return value;
        }
    }
    public R getLocked(){
        return getLocked(null);
    }
    public R get(){
        return get(null);
    }
    public R get(T param){
        if(value!=null){
            return value;
        }
        value = supplier.apply(param);
        supplier=null;
        return value;
    }
}
