package io.ib67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ib67.util.Box;
import io.ib67.util.CatchingContext;
import io.ib67.util.serialization.bukkit.ItemStackSerializer;
import io.ib67.util.serialization.bukkit.LocationSerializer;
import jdk.nashorn.internal.objects.annotations.Function;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;

public class Util {
    private static Object gsonForBukkit;
    private static final byte[] lock = new byte[0];
    public static class A<T> {
        public final int a = 1 ;
    }

    private Util(){
    }
    public static Gson gsonForBukkit(){
        synchronized (lock){
            if(gsonForBukkit==null){
                gsonForBukkit = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Location.class,new LocationSerializer())
                        .registerTypeAdapter(ItemStack.class,new ItemStackSerializer())
                        .create();

            }
        }
        return (Gson) gsonForBukkit;
    }
    @SneakyThrows
    public static String readAll(InputStream stream){
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[4096];
        int i;
        while((i = stream.read(buffer)) != -1){
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
