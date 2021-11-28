package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RepairAllOther {

    Repairer plugin;

    public RepairAllOther(Repairer plugin) {
        this.plugin = plugin;
    }

    public boolean repairAllOtherCommand(Player player, String[] args) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Player target = Bukkit.getPlayerExact(args[1]);
        Economy economy = Repairer.getInstance().getEconomy();
        final List<ItemStack> items = new ArrayList<>();
        if (!player.hasPermission("repairer.all.other")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
            return true;
        }
        for (ItemStack inventory : target.getInventory().getContents()) {
            if (inventory != null && !inventory.getType().isEdible() && inventory.getType() != Material.AIR && !inventory.getType().isBlock() && inventory.getType().getMaxDurability() > 0 && inventory.getDurability() != 0) {
                items.add(inventory);
            }
        }
        if (items.size() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noitemstorepair")));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherallinvalid")));
            return true;
        }
        if (args.length == 3) {
            if (player.hasPermission("repairer.all.other.money")) {
                if (args[2] != null) {
                    if (economy.has(target, Double.parseDouble(args[2]))) {
                        economy.withdrawPlayer(target, Double.parseDouble(args[2]));
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherallinvalid")));
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairallcantafford")));
                        return true;
                    }
                }
            }
            else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
                return true;
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedall")));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherall")));
        for (ItemStack inventory : items) inventory.setDurability((short) 0);
        target.updateInventory();
        return true;
    }
}
