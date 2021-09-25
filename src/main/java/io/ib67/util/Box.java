package io.ib67.util;

public class Box<T> {
    private Object boxed;
    private Box(T t){
        this.boxed=t;
    }
    private Box(){

    }
    public static <T> Box<T> of(T t){
        return new Box<>(t);
    }
    @SuppressWarnings("unchecked")
    public T get(){
        return (T) boxed;
    }
    public <R> Box<R> castTo(R object){
        Box<R> box = new Box<>();
        box.boxed = object.getClass().cast(boxed);
        return box;
    }
    public <R> Box<R> castTo(Class<R> rClass){
        Box<R> box = new Box<>();
        box.boxed = this.boxed;
        return box;
    }
}
