package io.ib67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ib67.util.serialization.bukkit.ItemStackSerializer;
import io.ib67.util.serialization.bukkit.LocationSerializer;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.InputStream;

public class Util {
    private static Gson gsonForBukkit;
    private Util(){
    }
    public static Gson gsonForBukkit(){
        synchronized (gsonForBukkit){
            if(gsonForBukkit==null){
                gsonForBukkit = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Location.class,new LocationSerializer())
                        .registerTypeAdapter(ItemStack.class,new ItemStackSerializer())
                        .create();

            }
        }
        return gsonForBukkit;
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
}
