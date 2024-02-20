package cn.superiormc.enchantmentslots;

import cn.superiormc.enchantmentslots.commands.MainCommand;
import cn.superiormc.enchantmentslots.commands.MainTab;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.listeners.EnchantGUIEnchantListener;
import cn.superiormc.enchantmentslots.listeners.PlayerClickListener;
import cn.superiormc.enchantmentslots.listeners.PlayerEnchantListener;
import cn.superiormc.enchantmentslots.listeners.PlayerInventoryListener;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.papi.PlaceholderAPIExpansion;
import cn.superiormc.enchantmentslots.protolcol.GeneralProtolcol;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class EnchantmentSlots extends JavaPlugin {

    public static EnchantmentSlots instance;

    public static boolean demoVersion = false;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        GeneralProtolcol.init();
        Messages.init();
        if (CommonUtil.checkPluginLoad("PlaceholderAPI")) {
            PlaceholderAPIExpansion.papi = new PlaceholderAPIExpansion(this);
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into PlaceholderAPI...");
            if (PlaceholderAPIExpansion.papi.register()){
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fFinished hook!");
            }
        }
        ExtraSlotsItem.init();
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is loaded. Author: PQguanfang.");
    }

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is disabled. Author: PQguanfang.");
    }

    private void registerEvents() {
        if (ConfigReader.getEnchantItemTrigger()) {
            Bukkit.getPluginManager().registerEvents(new PlayerEnchantListener(), this);
        }
        if (ConfigReader.getInventoryClickTrigger()) {
            Bukkit.getPluginManager().registerEvents(new PlayerClickListener(), this);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), this);
        if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("EnchantGui")) {
            Bukkit.getPluginManager().registerEvents(new EnchantGUIEnchantListener(), this);
        }
    }

    public void registerCommands() {
        Objects.requireNonNull(Bukkit.getPluginCommand("enchantmentslots")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("enchantmentslots")).setTabCompleter(new MainTab());
    }
}
