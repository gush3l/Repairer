package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Reload {
    Repairer plugin;

    public Reload(Repairer plugin) {
        this.plugin = plugin;
    }

    public boolean reloadCommand(Player player) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Repairer.getInstance().reloadConfig();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.reload")));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.reload")));
        return true;
    }
}

