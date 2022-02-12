package io.ib67.util.bukkit;

import io.ib67.Util;
import io.ib67.util.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    public static ColoredString pattern = new ColoredString("&8[&b%s&8] &f");
    private static Lazy<?, String> realPrefix = Lazy.by(() -> Util.BukkitAPI.currentPlugin().getName());
    public static void info(String message){
        Bukkit.getConsoleSender().sendMessage(realPrefix.get()+new ColoredString(message));
    }
    public static void warn(String message){
        Bukkit.getConsoleSender().sendMessage(realPrefix.get()+ ChatColor.RED+new ColoredString(message));
    }
}
