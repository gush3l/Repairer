package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RepairHandOther {

    Repairer plugin;

    public RepairHandOther(Repairer plugin) {
        this.plugin = plugin;
    }

    public boolean repairHandOtherCommand(Player player, String[] args) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Player target = Bukkit.getPlayer(args[1]);
        Economy economy = Repairer.getInstance().getEconomy();
        if (!player.hasPermission("repairer.hand.other")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
            return true;
        }
        if (target.getItemInHand().getType().isEdible() || target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getType().isBlock() || target.getItemInHand().getDurability() == 0 || target.getItemInHand().getType().getMaxDurability() <= 0) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invaliditem")));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhandinvalid")));
            return true;
        }
        if (args.length == 3) {
            if (player.hasPermission("repairer.hand.other.money")) {
                if (args[2] != null) {
                    if (economy.has(target, Double.parseDouble(args[2]))) {
                        economy.withdrawPlayer(target, Double.parseDouble(args[2]));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhandinvalid")));
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairhandcantafford")));
                        return true;
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
                return true;
            }
        }
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedhand")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhand")));
        target.getItemInHand().setDurability((short) 0);
        target.updateInventory();
        return true;
    }
}
