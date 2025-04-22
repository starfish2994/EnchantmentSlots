package cn.superiormc.enchantmentslots.protolcol;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.methods.AddLore;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.*;
import cn.superiormc.enchantmentslots.protolcol.eco.EcoDisplayModule;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

public abstract class GeneralProtolcol {

    public static void init() {
        String plugin = EnchantmentSlots.instance.getConfig().getString("settings.add-lore.use-listener-plugin",
                EnchantmentSlots.instance.getConfig().getString("settings.use-listener-plugin", "packetevents"));
        if (plugin.equals("packetevents") &&
                CommonUtil.checkPluginLoad("packetevents")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into packetevents....");
            PacketEvents.getAPI().getEventManager().registerListener(new SetSlots(), ConfigManager.configManager.getPriority());
            PacketEvents.getAPI().getEventManager().registerListener(new WindowItem(), ConfigManager.configManager.getPriority());
            PacketEvents.getAPI().getEventManager().registerListener(new WindowMerchant(), ConfigManager.configManager.getPriority());
            PacketEvents.getAPI().getEventManager().registerListener(new SetCreativeSlots(), ConfigManager.configManager.getPriority());
            AddLore.lorePrefix = ConfigManager.configManager.getString("settings.add-lore.lore-prefix", "§y");
        } else if (plugin.equals("eco") &&
                CommonUtil.checkPluginLoad("eco")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into eco....");
            EcoDisplayModule.init();
            AddLore.lorePrefix = "§z";
        } else {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found any packet " +
                    "listener plugin, enchantment slot won't displayed in your server!");
        }
    }

}
