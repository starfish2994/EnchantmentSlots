package cn.superiormc.enchantmentslots.protolcol;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.listeners.PlayerClickListener;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.*;
import cn.superiormc.enchantmentslots.protolcol.eco.EcoDisplayModule;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import org.bukkit.Bukkit;

public abstract class GeneralProtolcol {

    public static void init() {
        String plugin = EnchantmentSlots.instance.getConfig().getString("settings.add-lore.use-listener-plugin",
                EnchantmentSlots.instance.getConfig().getString("settings.use-listener-plugin", "ProtocolLib"));
        if (plugin.equals("ProtocolLib") &&
                CommonUtil.checkPluginLoad("ProtocolLib")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into ProtocolLib....");
            new SetCreativeSlots();
            new SetSlots();
            new WindowItem();
            Bukkit.getPluginManager().registerEvents(new PlayerClickListener(), EnchantmentSlots.instance);
            ItemModify.lorePrefix = "§y";
        } else if (plugin.equals("eco") &&
                CommonUtil.checkPluginLoad("eco")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into eco....");
            EcoDisplayModule.init();
            ItemModify.lorePrefix = "§z";
        } else {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found any packet " +
                    "listener plugin, enchantment slot won't displayed in your server!");
        }
    }

}
