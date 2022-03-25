package io.ib67.util.bukkit;

import io.ib67.util.Lazy;
import io.ib67.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {
    public static Text pattern = Text.of("&b%s&8: &f");
    public static Text modulePattern = Text.of("&b%s/%m&8: &f");
    private static Lazy<?, String> realPrefix = Lazy.by(() -> pattern.toString().replace("%s", Util.BukkitAPI.currentPlugin().getName()));
    private static Lazy<String, String> modulePrefix = Lazy.by(moduleName -> modulePattern.toString().replace("%s", Util.BukkitAPI.currentPlugin().getName()));

    @Deprecated
    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(realPrefix.get() + Text.colored(message));
    }

    public static void info(String module, String message) {
        Bukkit.getConsoleSender().sendMessage(modulePrefix.get().replace("%m", module) + Text.colored(message));
    }

    public static void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(realPrefix.get() + ChatColor.RED + Text.colored(message));
    }

    public static void warn(String module, String message) {
        Bukkit.getConsoleSender().sendMessage(modulePrefix.get().replace("%m", module) + ChatColor.RED + Text.colored(message));
    }
}
