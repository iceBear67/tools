package io.ib67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ib67.util.CatchingContext;
import io.ib67.util.EnvType;
import io.ib67.util.Functional;
import io.ib67.util.Lazy;
import io.ib67.util.bukkit.Log;
import io.ib67.util.serialization.bukkit.ItemStackSerializer;
import io.ib67.util.serialization.bukkit.LocationSerializer;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public class Util {
    private static Object gsonForBukkit;
    private static final byte[] lock = new byte[0];
    public static final EnvType ENV = Functional.from(() -> {
        try {
            Class.forName("org.bukkit.Bukkit");
            return EnvType.BUKKIT;
        } catch (Throwable t) {
            return EnvType.APP;
        }
    });

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
        private static final Lazy<?, JavaPlugin> CURRENT_PLUGIN = Lazy.by(() -> {
            ClassLoader t = Log.class.getClassLoader();
            String str = Util.readAll(Objects.requireNonNull(t.getResourceAsStream("plugin.yml")));
            String pluginName = Arrays.stream(str.split("\n")).map(e -> e.split(": ")).filter(e -> e.length == 2).filter(e -> e[0].equals("name")).findFirst().orElseThrow(() -> new NullPointerException("Not a valid plugin.yml or not in a plugin?"))[1];
            return (JavaPlugin) Bukkit.getPluginManager().getPlugin(pluginName);
        });

        public static JavaPlugin currentPlugin() {
            return CURRENT_PLUGIN.getLocked();
        }

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
