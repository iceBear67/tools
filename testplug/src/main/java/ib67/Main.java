package ib67;

import io.ib67.util.bukkit.Text;
import io.ib67.util.bukkit.Texts;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        saveDefaultConfig();
        reloadConfig();
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
