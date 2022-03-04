package ib67;

import io.ib67.util.bukkit.Text;
import io.ib67.util.bukkit.Texts;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();
        reloadConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof PlayerInventory) {
            Bukkit.getServer().getPluginManager().callEvent(new InventoryClickEvent(event.getView(), InventoryType.SlotType.CONTAINER, 1, ClickType.LEFT, InventoryAction.CLONE_STACK));
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Text.from("messages")
                .visit(Texts.readFromConfig())
                .map("text", "&m&n Some polluted texts")
                .line("Greetings! ")
                .toString()
        );
        return true;
    }
}
