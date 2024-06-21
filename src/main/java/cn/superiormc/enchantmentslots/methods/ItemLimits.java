package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemLimits {

    public static final NamespacedKey ENCHANTMENT_SLOTS_KEY = new NamespacedKey(EnchantmentSlots.instance, "enchantment_slots");

    public static int getMaxEnchantments(ItemStack item, int defaultSlot, String itemID) {
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (tempMeta == null) {
                return 0;
            }
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            if (canEnchant(item, itemID)) {
                return defaultSlot;
            }
            else {
                return 0;
            }
        }
        return meta.getPersistentDataContainer().get(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER);
    }

    public static int getRealMaxEnchantments(ItemStack item) {
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (tempMeta == null) {
                return 0;
            }
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return 0;
        }
        return meta.getPersistentDataContainer().get(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER);
    }

    public static void setMaxEnchantments(ItemStack item, int maxEnchantments) {
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER, maxEnchantments);
        item.setItemMeta(meta);
    }

    public static List<String> enchantItems = null;

    public static List<String> blackItems = null;

    public static boolean canEnchant(ItemStack itemStack, String itemID) {
        if (itemID == null) {
            itemID = "-null";
        }
        if (enchantItems == null) {
            enchantItems = ConfigReader.getItemCanBeEnchantedWhiteList();
            if (enchantItems.isEmpty()) {
                enchantItems = new ArrayList<>();
            }
        }
        if (blackItems == null) {
            blackItems = ConfigReader.getItemCanBeEnchantedBlackList();
            if (blackItems.isEmpty()) {
                blackItems = new ArrayList<>();
            }
        }
        if (!enchantItems.isEmpty()) {
            for (String tempVal1 : enchantItems) {
                if (tempVal1.equalsIgnoreCase(itemStack.getType().name()) || tempVal1.equalsIgnoreCase(itemID)) {
                    for (String tempVal2 : blackItems) {
                        if (tempVal2.equalsIgnoreCase(itemID)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        } else if (!blackItems.isEmpty()) {
            for (String tempVal2 : blackItems) {
                if (tempVal2.equalsIgnoreCase(itemStack.getType().name()) || tempVal2.equalsIgnoreCase(itemID)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
