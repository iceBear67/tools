package io.ib67.util.bukkit;

import io.ib67.Util;
import io.ib67.util.EnvType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.function.UnaryOperator;

public interface Text {

    static Text of(CharSequence charSequence) {
        if (Util.ENV == EnvType.BUKKIT) {
            return new BukkitText(charSequence);
        }
        throw new UnsupportedOperationException("unsupported");
    }

    static Text from(Collection<String> strings) {
        BukkitText bk = new BukkitText("");
        strings.forEach(bk::join);

        return bk;
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
                BukkitText bkText = new BukkitText("");
                Collection<?> coll = (Collection<?>) obj;
                if (!coll.stream().allMatch(e -> e instanceof String)) {
                    throw new IllegalArgumentException("collection must contain only strings");
                }
                Collection<String> colls = (Collection<String>) coll;
                colls.forEach(bkText::join);
                return bkText;
            }
            return new BukkitText(obj.toString());

        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    Text stripAllColor();

    Text stripColor(CharSequence string);

    default Text join(CharSequence... charSequence) {
        for (CharSequence sequence : charSequence) {
            join(sequence);
        }
        return this;
    }

    Text visit(UnaryOperator<String> placeholderOper);

    Text placeholder(String k, Object v);

    Text join(CharSequence charSequence);

    Text reverse();

    Text trim();

    Text map(UnaryOperator<String> mapper);

    @Override
    String toString();
}
