package io.ib67.util.bukkit;

import io.ib67.util.EnvType;
import io.ib67.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface Text {

    static Text of(CharSequence charSequence) {
        if (Util.ENV == EnvType.BUKKIT) {
            return new BukkitText(charSequence);
        }
        throw new UnsupportedOperationException("unsupported");
    }

    static String colored(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    static Text from(Collection<String> strings) {
        BukkitText bk = new BukkitText("");
        strings.forEach(bk::join);

        return bk;
    }

    static String stripColor(String s) {
        return new BukkitText(s).stripAllColor().toString();
    }

    static Text from(String configKey) {
        if (Util.ENV == EnvType.BUKKIT) {
            JavaPlugin plugin = Util.BukkitAPI.currentPlugin();
            FileConfiguration cfg = plugin.getConfig();
            Object obj = cfg.get(configKey);
            if (obj == null) {
                return new BukkitText("");
            }
            if (obj instanceof Collection) {
                Collection<?> coll = (Collection<?>) obj;
                if (!coll.stream().allMatch(e -> e instanceof String)) {
                    throw new IllegalArgumentException("collection must contain only strings");
                }
                Collection<String> colls = (Collection<String>) coll;
                return new BukkitText(colls.stream().collect(Collectors.joining("\n")));
            }
            return new BukkitText(obj.toString());

        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    Text line(CharSequence s);

    Text stripAllColor();

    Text stripColor(CharSequence string);

    default Text join(CharSequence... charSequence) {
        for (CharSequence sequence : charSequence) {
            join(sequence);
        }
        return this;
    }

    Text visit(UnaryOperator<String> placeholderOper);

    Text map(String k, Object v);

    Text join(CharSequence charSequence);

    Text reverse();

    Text trim();

    Text map(UnaryOperator<String> mapper);

    @Override
    String toString();
}
