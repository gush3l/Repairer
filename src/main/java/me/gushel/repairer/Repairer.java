package me.gushel.repairer;

import me.gushel.repairer.Commands.Repair;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public final class Repairer extends JavaPlugin {

    private Economy econ;
    private static Repairer plugin;

    @Override
    public void onEnable() {
        this.getCommand("repair").setExecutor(new Repair(this));
        System.out.println("[Repairer] Plugin started corectly!");
        this.saveDefaultConfig();
        plugin = this;
        Config.setup();
        Config.get().addDefault("Messages.noperm", "&6&lRepairer &8&l| &f&lYou can't do that!");
        Config.get().addDefault("Messages.oncooldownhand", "&6&lRepairer &8&l| &f&lYou can't use the repair hand command yet! You need to wait %TimeLeftHand% seconds!");
        Config.get().addDefault("Messages.oncooldownall", "&6&lRepairer &8&l| &f&lYou can't use the repair all command yet! You need to wait %TimeLeftAll% seconds!");
        Config.get().addDefault("Messages.invaliditem", "&6&lRepairer &8&l| &f&lThe item in your hand cannot be repaired!");
        Config.get().addDefault("Messages.repairedhand", "&6&lRepairer &8&l| &f&lThe item in your hand has been repaired!");
        Config.get().addDefault("Messages.repairhandcantafford", "&6&lRepairer &8&l| &f&lYou don't afford to execute a repair hand!");
        Config.get().addDefault("Messages.repairedall", "&6&lRepairer &8&l| &f&lThe items in your inventory have been repaired!");
        Config.get().addDefault("Messages.repairallcantafford", "&6&lRepairer &8&l| &f&lYou don't afford to execute a repair all!");
        Config.get().addDefault("Messages.noitemstorepair", "&6&lRepairer &8&l| &f&lYou don't have any items that need repairing.");
        Config.get().addDefault("Messages.otherhand", "&6&lRepairer &8&l| &f&lRepaired the item in the hand of that player.");
        Config.get().addDefault("Messages.otherall", "&6&lRepairer &8&l| &f&lRepaired the items in the inventory of that player.");
        Config.get().addDefault("Messages.otherhandinvalid","&6&lRepairer &8&l| &f&lCouldn't repair the item that was in the player's hand.");
        Config.get().addDefault("Messages.otherallinvalid","&6&lRepairer &8&l| &f&lThe player doesn't have any item to repair in their inventory.");
        Config.get().addDefault("Messages.notonline", "&6&lRepairer &8&l| &f&lThis player is not online!");
        Config.get().addDefault("Messages.reload", "&6&lRepairer &8&l| &f&lPlugin config reloaded corectly!");
        Config.get().addDefault("Messages.help", getConfig().getStringList("Messages.help"));
        Config.get().addDefault("Settings.repairhandcooldown", "10");
        Config.get().addDefault("Settings.repairhandcost", "100");
        Config.get().addDefault("Settings.repairallcooldown", "30");
        Config.get().addDefault("Settings.repairallcost", "3600");
        Config.get().options().copyDefaults(true);
        Config.save();
        if (!setupEconomy()) {
            this.getLogger().severe("[Repairer] Plugin disabled due to no Vault found!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    public static Repairer getInstance(){
        return plugin;
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        System.out.println("[Repairer] Plugin disabled corectly!");
    }
}