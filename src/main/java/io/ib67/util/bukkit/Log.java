package io.ib67.util.bukkit;

import io.ib67.Util;
import io.ib67.util.Lazy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    public static Text pattern = Text.of("&b%s&8: &f");
    private static Lazy<?, String> realPrefix = Lazy.by(() -> pattern.toString().replace("%s", Util.BukkitAPI.currentPlugin().getName()));

    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(realPrefix.get() + Text.colored(message));
    }

    public static void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(realPrefix.get() + ChatColor.RED + Text.colored(message));
    }
}
