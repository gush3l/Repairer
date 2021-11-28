package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairAll {

    Map<String, Long> repairall = new HashMap<>();

    Repairer plugin;

    public RepairAll(Repairer plugin) {
        this.plugin = plugin;
    }

    public boolean repairAllCommand(Player player) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Economy economy = Repairer.getInstance().getEconomy();
        final List<ItemStack> items = new ArrayList<>();
        if (!player.hasPermission("repairer.all")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
            return true;
        }
        if (!player.hasPermission("repairer.all.cooldown.bypass")) {
            if (repairall.containsKey(player.getName())) {
                if (repairall.get(player.getName()) > System.currentTimeMillis()) {
                    long timeleftall = (repairall.get(player.getName()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.oncooldownall").replace("%TimeLeftAll%", String.valueOf(timeleftall))));
                    return true;
                }
            }
        }
        for (ItemStack inventory : player.getInventory().getContents()) {
            if (inventory != null && !inventory.getType().isEdible() && inventory.getType() != Material.AIR && !inventory.getType().isBlock() && inventory.getType().getMaxDurability() > 0 && inventory.getDurability() != 0) {
                items.add(inventory);
            }
        }
        if (!player.hasPermission("repairer.all.money.bypass")) {
            if (!economy.has(player, Double.parseDouble(config.getString("Settings.repairallcost")))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairallcantafford")));
                return true;
            }
        }
        if (items.size() == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noitemstorepair")));
            return true;
        }
        if (!player.hasPermission("repairer.all.cooldown.bypass")) {
            repairall.put(player.getName(), System.currentTimeMillis() + (Integer.parseInt(config.getString("Settings.repairallcooldown")) * 1000));
        }
        if (!player.hasPermission("repairer.all.money.bypass")) {
            economy.withdrawPlayer(player, Double.parseDouble(config.getString("Settings.repairallcost")));
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedall")));
        for (ItemStack inventory : items) inventory.setDurability((short) 0);
        player.updateInventory();
        return true;
    }
}
