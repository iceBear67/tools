package io.ib67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ib67.util.serialization.bukkit.ItemStackSerializer;
import io.ib67.util.serialization.bukkit.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

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
}
