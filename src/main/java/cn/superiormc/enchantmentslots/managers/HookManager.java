package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.enchants.*;
import cn.superiormc.enchantmentslots.hooks.items.*;
import cn.superiormc.enchantmentslots.hooks.items.AbstractItemHook;
import cn.superiormc.enchantmentslots.methods.AddLore;
import cn.superiormc.enchantmentslots.methods.EnchantsUtil;
import cn.superiormc.enchantmentslots.papi.PlaceholderAPIExpansion;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import com.loohp.interactivechat.api.InteractiveChatAPI;
import com.loohp.interactivechat.objectholders.ICPlayer;
import com.loohp.interactivechat.objectholders.ICPlayerFactory;
import me.arasple.mc.trchat.module.internal.hook.HookPlugin;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class HookManager {

    public static HookManager hookManager;

    private Map<String, AbstractItemHook> itemHooks;

    private Map<String, AbstractEnchantHook> enchantHooks;

    private final Map<Enchantment, String> enchantmentNameCache = new HashMap<>();

    public HookManager() {
        hookManager = this;
        initNormalHook();
        initItemHook();
        initEnchantHook();
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
                return AddLore.addLore(itemStack, icPlayer.getLocalPlayer());
            });
        }
        if (CommonUtil.checkPluginLoad("TrChat")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into TrChat...");
            HookPlugin.INSTANCE.registerDisplayItemHook("EnchantmentSlots", AddLore::addLore);
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

    private void initEnchantHook() {
        enchantHooks = new HashMap<>();
        if (CommonUtil.checkPluginLoad("EcoEnchants")) {
            registerNewEnchantHook("EcoEnchants", new EnchantEcoEnchantsHook());
        }
        if (CommonUtil.checkPluginLoad("Aiyatsbus")) {
            registerNewEnchantHook("Aiyatsbus", new EnchantAiyatsbusHook());
        }
        if (CommonUtil.checkPluginLoad("ExcellentEnchants")) {
            if (CommonUtil.getClass("su.nightexpress.excellentenchants.api.enchantment.EnchantmentData")) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §6Warning: Seems that you are using ExcellentEnchants old version, enabled compatibility mode, " +
                        "this mode will be removed in future updates, please consider update it to latest.");
                registerNewEnchantHook("ExcellentEnchants", new EnchantExcellentEnchantsLegacyHook());
            } else {
                registerNewEnchantHook("ExcellentEnchants", new EnchantExcellentEnchantsHook());
            }
        }
    }

    public void registerNewItemHook(String pluginName,
                                    AbstractItemHook itemHook) {
        if (!itemHooks.containsKey(pluginName)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into " + pluginName + ".");
            itemHooks.put(pluginName, itemHook);
        }
    }

    public void registerNewEnchantHook(String pluginName,
                                    AbstractEnchantHook enchantHook) {
        if (!enchantHooks.containsKey(pluginName)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into " + pluginName + ".");
            enchantHooks.put(pluginName, enchantHook);
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

    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player, boolean showTierColor) {
        try {
            if (enchantmentNameCache.containsKey(enchantment)) {
                return enchantmentNameCache.get(enchantment);
            }
            String enchantmentName = ConfigManager.configManager.getString("enchant-name." + enchantment.getKey().getKey(), enchantment.getKey().getKey());
            if (enchantmentName.equals(enchantment.getKey().getKey())) {
                for (AbstractEnchantHook enchantHook : enchantHooks.values()) {
                    String tempVal1;
                    if (showTierColor) {
                        tempVal1 = enchantHook.getEnchantName(item, enchantment, player);
                    } else {
                        tempVal1 = enchantHook.getRawEnchantName(enchantment);
                    }
                    if (tempVal1 != null) {
                        return tempVal1;
                    }
                }
            }
            enchantmentNameCache.put(enchantment, TextUtil.parse(enchantmentName));
            return enchantmentName;
        } catch (Throwable throwable) {
            return "ERROR";
        }
    }

    public Map<Enchantment, Integer> sortEnchantments(ItemMeta meta) {
        if (enchantHooks.values().isEmpty()) {
            return EnchantsUtil.getEnchantments(meta, false);
        }
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (AbstractEnchantHook enchantHook : enchantHooks.values()) {
            enchantments = enchantHook.sortEnchants(meta);
        }
        return enchantments;
    }
}