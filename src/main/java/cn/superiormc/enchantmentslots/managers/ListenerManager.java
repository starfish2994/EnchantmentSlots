package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.listeners.*;
import cn.superiormc.enchantmentslots.methods.AddLore;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.SetCreativeSlots;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.SetSlots;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.WindowItem;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.WindowMerchant;
import cn.superiormc.enchantmentslots.protolcol.eco.EcoDisplayModule;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

public class ListenerManager {

    public static ListenerManager listenerManager;

    public ListenerManager() {
        listenerManager = this;
        registerListeners();
        if (LicenseManager.licenseManager.checkJarFiles()) {
            registerPacketListeners();
        }
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

    private void registerPacketListeners() {
        String plugin = EnchantmentSlots.instance.getConfig().getString("settings.add-lore.use-listener-plugin",
                EnchantmentSlots.instance.getConfig().getString("settings.use-listener-plugin", "packetevents"));
        if (plugin.equals("packetevents") && CommonUtil.checkPluginLoad("packetevents")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into packetevents....");
            PacketEventsListener.registerPacketEventsListener();
            AddLore.lorePrefix = ConfigManager.configManager.getString("settings.add-lore.lore-prefix", "§y");
        } else if (plugin.equals("eco") && CommonUtil.checkPluginLoad("eco")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into eco....");
            EcoDisplayModule.init();
            AddLore.lorePrefix = "§z";
        } else {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found any packet " +
                    "listener plugin, enchantment slot won't displayed in your server!");
        }
    }
}

class PacketEventsListener {
    public static void registerPacketEventsListener() {
        PacketEvents.getAPI().getEventManager().registerListener(new SetSlots(), ConfigManager.configManager.getPriority());
        PacketEvents.getAPI().getEventManager().registerListener(new WindowItem(), ConfigManager.configManager.getPriority());
        PacketEvents.getAPI().getEventManager().registerListener(new WindowMerchant(), ConfigManager.configManager.getPriority());
        PacketEvents.getAPI().getEventManager().registerListener(new SetCreativeSlots(), ConfigManager.configManager.getPriority());
    }
}
