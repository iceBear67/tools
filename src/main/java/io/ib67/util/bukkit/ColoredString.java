package io.ib67.util.bukkit;

import org.bukkit.ChatColor;

public class ColoredString {
    private StringBuilder colored;

    public static String of(String legacyText) {
        return new ColoredString(legacyText).toString();
    }

    public ColoredString(String toBeColored) {
        colored = new StringBuilder(toBeColored);
    }

    public ColoredString append(String str) {
        colored.append(str);
        return this;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&',colored.toString());
    }
}
