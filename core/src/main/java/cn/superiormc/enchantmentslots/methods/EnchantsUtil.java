package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.utils.NumberUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnchantsUtil {

    private static final Map<Integer, String> enchantmentLevelCache = new HashMap<>();

    public static int getUsedSlot(ItemStack item) {
        return getUsedSlot(getEnchantments(item, false).keySet());
    }

    public static int getUsedSlot(ItemMeta meta) {
        return getUsedSlot(getEnchantments(meta, false).keySet());
    }

    public static int getUsedSlot(Set<Enchantment> enchantments) {
        int nowSlot = 0;
        for (Enchantment enchantment : enchantments) {
            int usedSlot = getUsedSlot(enchantment);
            nowSlot = nowSlot + usedSlot;
        }
        return nowSlot;
    }

    public static int getUsedSlot(Enchantment enchantment) {
        return ConfigManager.configManager.config.getInt("enchant-used-slot.values." + enchantment.getKey().getKey(), 1);
    }

    public static String getUsedSlotPlaceholder(Enchantment enchantment) {
        int value = getUsedSlot(enchantment);
        String result = ConfigManager.configManager.config.getString("enchant-used-slot.placeholder." + value, String.valueOf(value));
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.auto-add-space", true) && !result.isEmpty()) {
            return " " + result;
        }
        return result;
    }

    @NotNull
    public static Map<Enchantment, Integer> getEnchantments(@NotNull ItemStack itemStack, boolean sort) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return new HashMap<>();
        }
        return getEnchantments(itemMeta, sort);
    }

    @NotNull
    public static Map<Enchantment, Integer> getEnchantments(@NotNull ItemMeta itemMeta, boolean sort) {
        if (!ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.sort", true)) {
            sort = false;
        }
        if (sort) {
            return HookManager.hookManager.sortEnchantments(itemMeta);
        } else {
            Map<Enchantment, Integer> enchantments;
            if (itemMeta instanceof EnchantmentStorageMeta storageMeta) {
                enchantments = storageMeta.getStoredEnchants();
                if (enchantments.isEmpty()) {
                    enchantments = itemMeta.getEnchants();
                }
            } else {
                enchantments = itemMeta.getEnchants();
            }
            return enchantments;
        }
    }

    public static String getEnchantmentLevel(Enchantment enchantment, int level) {
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.level-hide-one", false) && level == enchantment.getMaxLevel() && enchantment.getMaxLevel() == 1) {
            return "";
        }
        String result = getEnchantLevel(level);
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.auto-add-space", true) && !result.isEmpty()) {
            return " " + result;
        }
        return result;
    }

    public static String getEnchantmentLevelRoman(Enchantment enchantment, int level) {
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.level-hide-one", false) && level == enchantment.getMaxLevel() && enchantment.getMaxLevel() == 1) {
            return "";
        }
        String result = NumberUtil.convertToRoman(level);
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.enchants.auto-add-space", true) && !result.isEmpty()) {
            return " " + result;
        }
        return result;
    }

    public static String getEnchantLevel(int level) {
        if (enchantmentLevelCache.containsKey(level)) {
            return enchantmentLevelCache.get(level);
        }
        String levelName = ConfigManager.configManager.getString("enchant-level." + level, String.valueOf(level));
        enchantmentLevelCache.put(level, levelName);
        return levelName;
    }
}
