package io.ib67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ib67.util.CatchingContext;
import io.ib67.util.serialization.bukkit.ItemStackSerializer;
import io.ib67.util.serialization.bukkit.LocationSerializer;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.Path;

public class Util {
    private static Object gsonForBukkit;
    private static final byte[] lock = new byte[0];

    private Util() {
    }

    public static <T> T or(T t, T or) {
        return t == null ? or : t;
    }

    public static <T> CatchingContext<T> runCatching(ExceptedRunnable<T> runnable) {
        try {
            return new CatchingContext<>(runnable.run());
        } catch (Throwable t) {
            return new CatchingContext<>(t);
        }
    }

    @SneakyThrows
    public static String readAll(InputStream stream) {
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[4096];
        int i;
        while ((i = stream.read(buffer)) != -1) {
            builder.append(new String(buffer));
        }
        return builder.toString();
    }

    // fuck java 8
    public static class Java8Compat {
        @SneakyThrows
        public static String readString(Path path) {
            try (InputStream is = new FileInputStream(path.toFile())) {
                return readAll(is);
            }
        }

        @SneakyThrows
        public static void writeString(Path path, String string) {
            File file = path.toFile();
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(string.getBytes());
                os.flush();
            }

        }
    }

    public static class BukkitAPI {
        public static Gson gsonForBukkit() {
            if (gsonForBukkit == null) {
                gsonForBukkit = gsonBuilderForBukkit()
                        .create();

            }
            return (Gson) gsonForBukkit;
        }

        public static GsonBuilder gsonBuilderForBukkit() {
            return new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Location.class, new LocationSerializer())
                    .registerTypeAdapter(ItemStack.class, new ItemStackSerializer());
        }

        public static Class<?> ofNMSClass(String clazzName) {
            return runCatching(() -> Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + clazzName)).getResult();
        }
    }

    @FunctionalInterface
    public interface ExceptedRunnable<T> {
        T run() throws Throwable;
    }
}
