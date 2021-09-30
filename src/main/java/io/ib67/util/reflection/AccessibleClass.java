package io.ib67.util.reflection;

import io.ib67.Util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

public class AccessibleClass<T> {
    private static final MethodHandles.Lookup TRUSTED_LOOKUP;

    static {
        TRUSTED_LOOKUP = (MethodHandles.Lookup) AccessibleClass.of(MethodHandles.Lookup.class).field("IMPL_LOOKUP").get(null);
    }

    private Class<T> clazz;
    private Map<String, AccessibleField<T>> fields = new HashMap<>();

    private AccessibleClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <A> AccessibleClass<A> of(Class<A> clazz) {
        return new AccessibleClass<>(clazz);
    }

    public AccessibleField<T> field(String fieldName) {
        return fields.putIfAbsent(fieldName, new AccessibleField<>(clazz, fieldName));
    }

    public MethodHandle method(String name, MethodType type) {
        return Util.runCatching(() -> TRUSTED_LOOKUP.findVirtual(clazz, name, type)).getResult();
    }

}
