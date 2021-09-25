package io.ib67.util.reflection;

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * 用于访问 Field 的包装。
 * 访问方式有两种：
 * 1. 通过暴露出来的 getter / setter
 * 2. 使用 unsafe 设置
 * @param <T>
 */
public class AccessibleField<T> {
    private Class<T> clazz;
    @Getter
    private String fieldName;
    private MethodHandle setter;
    private MethodHandle getter;
    private long offset;
    @SneakyThrows
    public AccessibleField(Class<T> clazz, String fieldName){
        this.clazz=clazz;
        this.fieldName = fieldName;
        /**
         * Find Setters.
         */
        Field field = clazz.getDeclaredField(fieldName);
        Class<?> fieldType = field.getType();
        try {
            setter = MethodHandles.lookup().findVirtual(clazz, "set" + uppercaseFirst(fieldName), MethodType.methodType(void.class, fieldType));
        }catch(Throwable excepted){
            // Maybe there isn't a setter method.
            offset = Unsafe.objectFieldOffset(field);
        }
        try {
            getter = MethodHandles.lookup().findVirtual(clazz, "get" + uppercaseFirst(fieldName), MethodType.methodType(fieldType));
        }catch(Throwable excepted){
            // Maybe there isn't a Getter method.
            if(offset!=0L) offset = Unsafe.objectFieldOffset(field);
        }
    }

    @SneakyThrows
    public void set(T t,Object data){
        //Object d = processors.stream().map(e->e.fromDatabase(fieldName,data)).filter(Objects::nonNull).findFirst().orElse(data);

        if(setter!=null){
            setter.invokeExact(t,data);
        }
        assert offset!=0;
        Unsafe.putObject(t,offset,data);
    }
    @SneakyThrows
    public Object get(T t){
        Object data = getter==null?Unsafe.getObject(t,offset):getter.invokeExact();
        //return processors.stream().map(e->e.toDatabase(fieldName,data)).filter(Objects::nonNull).findFirst().orElse(data);
        return data;
    }
    private static final String uppercaseFirst(String str){
        char[] arr = str.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
