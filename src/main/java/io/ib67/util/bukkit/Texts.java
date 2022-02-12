package io.ib67.util.bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.function.UnaryOperator;

// constants.
public class Texts {
    public static final UnaryOperator<String> placeholderApi(Player player) {
        return t -> PlaceholderAPI.setPlaceholders(player, "%" + t + "%");
    }

}
