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
        Unsafe.ensureClassInitialized(MethodHandles.Lookup.class);
        TRUSTED_LOOKUP = (MethodHandles.Lookup) new AccessibleField<>(MethodHandles.Lookup.class, "IMPL_LOOKUP", true).get(null);
        System.out.println(TRUSTED_LOOKUP);
    }

    private Class<T> clazz;
    private Map<String, AccessibleField<T>> fields = new HashMap<>();

    private AccessibleClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <A> AccessibleClass<A> of(Class<A> clazz) {
        return new AccessibleClass<>(clazz);
    }

    public AccessibleField<T> virtualField(String fieldName) {
        return fields.computeIfAbsent(fieldName, f -> new AccessibleField<>(clazz, fieldName, false));
    }

    public AccessibleField<T> staticField(String fieldName) {
        return fields.computeIfAbsent(fieldName, f -> new AccessibleField<>(clazz, fieldName, true));
    }

    public MethodHandle method(String name, MethodType type) {
        return Util.runCatching(() -> TRUSTED_LOOKUP.findVirtual(clazz, name, type)).getResult();
    }

}
