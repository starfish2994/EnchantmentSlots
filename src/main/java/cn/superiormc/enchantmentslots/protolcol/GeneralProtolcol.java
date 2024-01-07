package cn.superiormc.enchantmentslots.protolcol;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.SetCreativeSlots;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.SetSlots;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.Test;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.WindowItem;
import cn.superiormc.enchantmentslots.protolcol.eco.EcoDisplayModule;
import cn.superiormc.enchantmentslots.utils.ItemModify;
import org.bukkit.Bukkit;

public abstract class GeneralProtolcol {

    public static String plugin;

    public static void init() {
        if (EnchantmentSlots.instance.getConfig().getString("settings.use-listener-plugin", "ProtocolLib")
                .equals("ProtocolLib") &&
                Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into ProtocolLib....");
            plugin = "ProtocolLib";
            Bukkit.getScheduler().runTaskAsynchronously(EnchantmentSlots.instance, () -> {
                new SetCreativeSlots();
                new SetSlots();
                new WindowItem();
                ItemModify.lorePrefix = "§y";
            });
        } else if (EnchantmentSlots.instance.getConfig().getString("settings.use-listener-plugin")
                .equals("eco") &&
                Bukkit.getPluginManager().isPluginEnabled("eco")) {
            plugin = "eco";
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into eco....");
            EcoDisplayModule.init();
            ItemModify.lorePrefix = "§z";
        } else {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found any packet " +
                    "listener plugin, enchantment slot won't displayed in your server!");
        }
    }

}
