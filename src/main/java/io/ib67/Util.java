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

import java.io.InputStream;

public class Util {
    private static Object gsonForBukkit;
    private static final byte[] lock = new byte[0];

    private Util() {
    }

    public static <T> T nullOrElse(T t, T or) {
        return t == null ? or : t;
    }

    public static class BukkitAPI {
        public static Gson gsonForBukkit() {
            synchronized (lock) {
                if (gsonForBukkit == null) {
                    gsonForBukkit = gsonBuilderForBukkit()
                            .create();

                }
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
    public static <T> CatchingContext<T> runCatching(ExceptedRunnable<T> runnable){
        try{
            return new CatchingContext<>(runnable.run());
        }catch(Throwable t){
            return new CatchingContext<>(t);
        }
    }

    @FunctionalInterface
    public interface ExceptedRunnable<T> {
        T run() throws Throwable;
    }
}
