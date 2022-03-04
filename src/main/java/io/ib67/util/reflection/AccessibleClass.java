package io.ib67.util.reflection;

import io.ib67.util.Util;

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

    @SuppressWarnings("unchecked")
    public T newInstance(Object... args) throws NoSuchMethodException {
        if (args.length == 0) {
            // find empty accessible valid constructor
            try {
                var con = clazz.getDeclaredConstructor();
                con.setAccessible(true);
                return con.newInstance();
            } catch (Throwable ignored) {

            }
            // failed.
            try {
                return (T) Unsafe.getUnsafe().allocateInstance(clazz);
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new NoSuchMethodException("No accessible constructor found");
            }
        }
        try {
            Class<?>[] paraTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paraTypes[i] = args[i].getClass();
            }
            var con = clazz.getDeclaredConstructor(paraTypes);
            con.setAccessible(true);
            return con.newInstance(args);
        } catch (Throwable ignored) {
            throw new NoSuchMethodException("No accessible constructor found for " + clazz.getName());
        }
    }
    public MethodHandle method(String name, MethodType type) {
        return Util.runCatching(() -> TRUSTED_LOOKUP.findVirtual(clazz, name, type)).getResult();
    }

}
