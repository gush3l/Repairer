package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RepairHand {

    Map<String, Long> repairhand = new HashMap<>();

    Repairer plugin;

    public RepairHand(Repairer plugin) { this.plugin = plugin; }

    public boolean repairHandCommand(Player player) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Economy economy = Repairer.getInstance().getEconomy();
        if (!player.hasPermission("repairer.hand")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noperm")));
            return true;
        }
        if (!player.hasPermission("repairer.hand.cooldown.bypass")) {
            if (repairhand.containsKey(player.getName())) {
                if (repairhand.get(player.getName()) > System.currentTimeMillis()) {
                    long timelefthand = (repairhand.get(player.getName()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.oncooldownhand").replace("%TimeLeftHand%", String.valueOf(timelefthand))));
                    return true;
                }
            }
        }
        if (player.getItemInHand().getType().isEdible() || player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getType().isBlock() || player.getItemInHand().getDurability() == 0 || player.getItemInHand().getType().getMaxDurability() <= 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invaliditem")));
            return true;
        }
        if (!player.hasPermission("repairer.hand.money.bypass")) {
            if (!economy.has(player, Double.parseDouble(config.getString("Settings.repairhandcost")))) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairhandcantafford")));
                return true;
            }
        }
        if (!player.hasPermission("repairer.hand.cooldown.bypass")) {
            repairhand.put(player.getName(), System.currentTimeMillis() + (Integer.parseInt(config.getString("Settings.repairhandcooldown")) * 1000));
        }
        if (!player.hasPermission("repairer.hand.money.bypass")) {
            economy.withdrawPlayer(player, Double.parseDouble(config.getString("Settings.repairhandcost")));
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedhand")));
        player.getItemInHand().setDurability((short) 0);
        player.updateInventory();
        return true;
    }

}