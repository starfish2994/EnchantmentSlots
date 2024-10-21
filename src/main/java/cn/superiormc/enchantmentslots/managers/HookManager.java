package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.items.*;
import cn.superiormc.enchantmentslots.hooks.items.AbstractItemHook;
import cn.superiormc.enchantmentslots.hooks.mythicchanger.AddESLore;
import cn.superiormc.enchantmentslots.hooks.mythicchanger.ResetSlot;
import cn.superiormc.enchantmentslots.hooks.mythicchanger.SetSlot;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.papi.PlaceholderAPIExpansion;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.mythicchanger.manager.ChangesManager;
import com.loohp.interactivechat.api.InteractiveChatAPI;
import com.loohp.interactivechat.objectholders.ICPlayer;
import com.loohp.interactivechat.objectholders.ICPlayerFactory;
import me.arasple.mc.trchat.module.internal.hook.HookPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HookManager {

    public static HookManager hookManager;

    private Map<String, AbstractItemHook> itemHooks;

    public HookManager() {
        hookManager = this;
        initNormalHook();
        initItemHook();
    }

    private void initNormalHook() {
        if (CommonUtil.checkPluginLoad("PlaceholderAPI")) {
            PlaceholderAPIExpansion.papi = new PlaceholderAPIExpansion(EnchantmentSlots.instance);
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into PlaceholderAPI...");
            if (PlaceholderAPIExpansion.papi.register()) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fFinished hook!");
            }
        }
        if (CommonUtil.checkPluginLoad("InteractiveChat")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into InteractiveChat...");
            InteractiveChatAPI.registerItemStackTransformProvider(EnchantmentSlots.instance, 10, (itemStack, uuid) -> {
                ICPlayer icPlayer = ICPlayerFactory.getICPlayer(uuid);
                return ItemModify.serverToClient(itemStack, icPlayer.getLocalPlayer());
            });
        }
        if (CommonUtil.checkPluginLoad("TrChat")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into TrChat...");
            HookPlugin.INSTANCE.registerDisplayItemHook("EnchantmentSlots", ItemModify::serverToClient);
        }
        if (CommonUtil.checkPluginLoad("ExcellentEnchants") && CommonUtil.getClass("su.nightexpress.excellentenchants.api.enchantment.EnchantmentData")) {
            EnchantmentSlots.eeLegacy = true;
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §7Seems that you are using ExcellentEnchants old version, enabled compatibility mode, " +
                    "this mode will be removed in future updates, please consider update it to latest.");
        }
        if (CommonUtil.checkPluginLoad("MythicChanger")) {
            MythicChangerHook.initMythicChanger();
        }
    }

    private void initItemHook() {
        itemHooks = new HashMap<>();
        if (CommonUtil.checkPluginLoad("ItemsAdder")) {
            registerNewItemHook("ItemsAdder", new ItemItemsAdderHook());
        }
        if (CommonUtil.checkPluginLoad("Oraxen")) {
            registerNewItemHook("Oraxen", new ItemOraxenHook());
        }
        if (CommonUtil.checkPluginLoad("MMOItems")) {
            registerNewItemHook("MMOItems", new ItemMMOItemsHook());
        }
        if (CommonUtil.checkPluginLoad("EcoItems")) {
            registerNewItemHook("EcoItems", new ItemEcoItemsHook());
        }
        if (CommonUtil.checkPluginLoad("EcoArmor")) {
            registerNewItemHook("EcoArmor", new ItemEcoArmorHook());
        }
        if (CommonUtil.checkPluginLoad("MythicMobs")) {
            registerNewItemHook("MythicMobs", new ItemMythicMobsHook());
        }
        if (CommonUtil.checkPluginLoad("eco")) {
            registerNewItemHook("eco", new ItemecoHook());
        }
        if (CommonUtil.checkPluginLoad("NeigeItems")) {
            registerNewItemHook("NeigeItems", new ItemNeigeItemsHook());
        }
        if (CommonUtil.checkPluginLoad("ExecutableItems")) {
            registerNewItemHook("ExecutableItems", new ItemExecutableItemsHook());
        }
    }

    public void registerNewItemHook(String pluginName,
                                    AbstractItemHook itemHook) {
        if (!itemHooks.containsKey(pluginName)) {
            itemHooks.put(pluginName, itemHook);
        }
    }

    public String parseItemID(ItemStack hookItem) {
        if (!hookItem.hasItemMeta()) {
            return hookItem.getType().name().toLowerCase();
        }
        for (AbstractItemHook itemHook : itemHooks.values()) {
            String tempVal1 = itemHook.getSimplyIDByItemStack(hookItem);
            if (tempVal1 != null) {
                return tempVal1;
            }
        }
        return hookItem.getType().name().toLowerCase();
    }
}

class MythicChangerHook {
    public static void initMythicChanger() {
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into MythicChanger...");
        ChangesManager.changesManager.registerNewRule(new SetSlot());
        ChangesManager.changesManager.registerNewRule(new ResetSlot());
        ChangesManager.changesManager.registerNewRule(new AddESLore());
    }
}
