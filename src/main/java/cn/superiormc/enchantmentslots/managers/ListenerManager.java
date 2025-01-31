package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.listeners.*;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import org.bukkit.Bukkit;

public class ListenerManager {

    public static ListenerManager listenerManager;

    public ListenerManager() {
        listenerManager = this;
        registerListeners();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerCacheListener(), EnchantmentSlots.instance);
        Bukkit.getPluginManager().registerEvents(new PlayerEnchantListener(), EnchantmentSlots.instance);
        Bukkit.getPluginManager().registerEvents(new PlayerAnvilListener(), EnchantmentSlots.instance);
        if (EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.InventoryClickEvent.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new PlayerClickListener(), EnchantmentSlots.instance);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), EnchantmentSlots.instance);
        if (CommonUtil.getMajorVersion(16)) {
            Bukkit.getPluginManager().registerEvents(new PlayerSmithListener(), EnchantmentSlots.instance);
        }
        if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("EnchantGui")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into EnchantGui...");
            Bukkit.getPluginManager().registerEvents(new EnchantGUIEnchantListener(), EnchantmentSlots.instance);
        }
        if (CommonUtil.checkPluginLoad("QuickShop-Hikari")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into QuickShop-Hikari...");
            Bukkit.getPluginManager().registerEvents(new QuickShopListener(), EnchantmentSlots.instance);
        }
    }
}
