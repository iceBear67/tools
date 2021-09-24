package io.ib67.util;

import java.util.function.Supplier;

public class Lazy<T> {
    private Supplier<T> supplier;
    private T value;
    private Lazy(Supplier<T> supplier){
        this.supplier = supplier;
    }
    public static <T> Lazy<T> by(Supplier<T> supplier){
        return new Lazy<>(supplier);
    }
    public T getLocked(){
        synchronized (value){
            if(value!=null){
                return value;
            }else{
                value = supplier.get();
                supplier = null;
                return value;
            }
        }
    }
    public T get(){
        if(value!=null){
            return value;
        }
        value = supplier.get();
        supplier=null;
        return value;
    }
}
