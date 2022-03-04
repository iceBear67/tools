package io.ib67.util.bukkit;

import io.ib67.util.Util;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

// constants.
public class Texts {

    public static final UnaryOperator<String> placeholderApi(Player player) {
        return t -> PlaceholderAPI.setPlaceholders(player, "%" + t + "%");
    }

    public static final UnaryOperator<String> readFromConfig() {
        return t -> Optional.ofNullable(Util.BukkitAPI.currentPlugin().getConfig().get(t))
                .map(Object::toString)
                .map(Text::of)
                .map(Objects::toString).orElse(null);
    }
}
