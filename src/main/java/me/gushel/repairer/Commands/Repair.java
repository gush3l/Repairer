package me.gushel.repairer.Commands;

import me.gushel.repairer.Repairer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Repair implements CommandExecutor {

    Repairer plugin;
    RepairHand repairHand;
    RepairHandOther repairHandOther;
    RepairAll repairAll;
    RepairAllOther repairAllOther;
    Reload reload;

    public Repair(Repairer passedPlugin){
        repairHand = new RepairHand(passedPlugin);
        repairHandOther = new RepairHandOther(passedPlugin);
        repairAll = new RepairAll(passedPlugin);
        repairAllOther = new RepairAllOther(passedPlugin);
        reload = new Reload(passedPlugin);
        this.plugin = passedPlugin;
    }
    public boolean onCommand(CommandSender sender ,Command cmd, String label,String[] args) {
        FileConfiguration config = Repairer.getInstance().getConfig();
        Economy economy = Repairer.getInstance().getEconomy();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("hand")) {
                    if (Bukkit.getPlayerExact(args[1]) != null) {
                        repairHandOther.repairHandOtherCommand(player, args);
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                    return true;
                }
                if (args[0].equalsIgnoreCase("all")) {
                    if (Bukkit.getPlayerExact(args[1]) != null) {
                        repairAllOther.repairAllOtherCommand(player, args);
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invalidsyntax")));
                return true;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("hand")) {
                    if (Bukkit.getPlayerExact(args[1]) != null) {
                        repairHandOther.repairHandOtherCommand(player, args);
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                    return true;
                }
                if (args[0].equalsIgnoreCase("all")) {
                    if (Bukkit.getPlayerExact(args[1]) != null) {
                        repairAllOther.repairAllOtherCommand(player, args);
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invalidsyntax")));
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("hand")) {
                    repairHand.repairHandCommand(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    reload.reloadCommand(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("all")) {
                    repairAll.repairAllCommand(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    for (String help : config.getStringList("Messages.help")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
                    }
                    return true;
                }
            }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invalidsyntax")));
                return true;
        }
        else{
            if (sender instanceof ConsoleCommandSender){
                if (args.length == 1){
                    if (args[0].equalsIgnoreCase("reload")){
                        Repairer.getInstance().reloadConfig();
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.reload")));
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("help")) {
                        for (String help : config.getStringList("Messages.help")) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', help));
                        }
                        return true;
                    }
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invalidsyntax")));
                    return true;
                }
                if (args.length == 2){
                    if (args[0].equalsIgnoreCase("hand")) {
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target.getItemInHand().getType().isEdible() || target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getType().isBlock() || target.getItemInHand().getDurability() == 0 || target.getItemInHand().getType().getMaxDurability() <= 0) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invaliditem")));
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhandinvalid")));
                                return true;
                            }
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedhand")));
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhand")));
                            target.getItemInHand().setDurability((short) 0);
                            target.updateInventory();
                            return true;
                        }
                        else{
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                            return true;
                        }
                    }
                    if (args[0].equalsIgnoreCase("all")) {
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            final List<ItemStack> items = new ArrayList<>();
                            for (ItemStack inventory : target.getInventory().getContents()) {
                                if (inventory != null && !inventory.getType().isEdible() && inventory.getType() != Material.AIR && !inventory.getType().isBlock() && inventory.getType().getMaxDurability() > 0 && inventory.getDurability() != 0) {
                                    items.add(inventory);
                                }
                            }
                            if (items.size() == 0) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noitemstorepair")));
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherallinvalid")));
                                return true;
                            }
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedall")));
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherall")));
                            for (ItemStack inventory : items) inventory.setDurability((short) 0);
                            target.updateInventory();
                            return true;
                        }
                        else{
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                            return true;
                        }
                    }
                }
                if (args.length == 3){
                    if (args[0].equalsIgnoreCase("hand")) {
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target.getItemInHand().getType().isEdible() || target.getItemInHand().getType() == Material.AIR || target.getItemInHand().getType().isBlock() || target.getItemInHand().getDurability() == 0 || target.getItemInHand().getType().getMaxDurability() <= 0) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.invaliditem")));
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhandinvalid")));
                                return true;
                            }
                            if (args[2] != null) {
                                if (economy.has(target, Double.parseDouble(args[2]))) {
                                    economy.withdrawPlayer(target, Double.parseDouble(args[2]));
                                } else {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhandinvalid")));
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairhandcantafford")));
                                    return true;
                                }
                            }
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedhand")));
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherhand")));
                            target.getItemInHand().setDurability((short) 0);
                            target.updateInventory();
                            return true;
                        }
                        else{
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                            return true;
                        }
                    }
                    if (args[0].equalsIgnoreCase("all")) {
                        if (Bukkit.getPlayerExact(args[1]) != null) {
                            Player target = Bukkit.getPlayer(args[1]);
                            final List<ItemStack> items = new ArrayList<>();
                            for (ItemStack inventory : target.getInventory().getContents()) {
                                if (inventory != null && !inventory.getType().isEdible() && inventory.getType() != Material.AIR && !inventory.getType().isBlock() && inventory.getType().getMaxDurability() > 0 && inventory.getDurability() != 0) {
                                    items.add(inventory);
                                }
                            }
                            if (items.size() == 0) {
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.noitemstorepair")));
                                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherallinvalid")));
                                return true;
                            }
                            if (args[2] != null) {
                                if (economy.has(target, Double.parseDouble(args[2]))) {
                                    economy.withdrawPlayer(target, Double.parseDouble(args[2]));
                                } else {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherallinvalid")));
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairallcantafford")));
                                    return true;
                                }
                            }
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.repairedall")));
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.otherall")));
                            for (ItemStack inventory : items) inventory.setDurability((short) 0);
                            target.updateInventory();
                            return true;
                        }
                        else{
                            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Messages.notonline")));
                            return true;
                        }
                    }
                }
                if (args.length == 0 || args.length > 3) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("Messages.invalidsyntax")));
                    return true;
                }
            }
        }
        return true;
    }
}
