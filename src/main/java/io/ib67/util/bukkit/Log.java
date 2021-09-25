package io.ib67.util.bukkit;

import io.ib67.Util;
import io.ib67.util.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Objects;

public class Log {
    public static ColoredString pattern = new ColoredString("&8[&b%s&8] &f");
    private static Lazy<?,String> realPrefix = Lazy.by(() ->{
        ClassLoader t = Log.class.getClassLoader();
        String str = Util.readAll(Objects.requireNonNull(t.getResourceAsStream("plugin.yml")));
        return pattern.toString().replaceAll("%s",Arrays.stream(str.split("\n")).map(e->e.split(": ")).filter(e->e.length==2).filter(e->e[0].equals("name")).findFirst().orElseThrow(()->new NullPointerException("Not a valid plugin.yml or not in a plugin?"))[1]);
    });
    public static void info(String message){
        Bukkit.getConsoleSender().sendMessage(realPrefix.get()+new ColoredString(message));
    }
    public static void warn(String message){
        Bukkit.getConsoleSender().sendMessage(realPrefix.get()+ ChatColor.RED+new ColoredString(message));
    }
}
