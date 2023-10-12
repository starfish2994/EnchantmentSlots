package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemLimits {

    public static final NamespacedKey ENCHANTMENT_SLOTS_KEY = new NamespacedKey(EnchantmentSlots.instance, "enchantment_slots");

    public static int getMaxEnchantments(Player player, ItemStack item) {
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (tempMeta == null) {
                return 0;
            }
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            if (canEnchant(item)) {
                return ConfigReader.getDefaultLimits(player);
            }
            else {
                return 0;
            }
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

    public static boolean canEnchant(ItemStack itemStack) {
        if (!ConfigReader.getAutoAddSlotsAutoCheck()) {
            return ConfigReader.getAutoAddSlotsItems().contains(itemStack.getType().name().toLowerCase()) ||
                    ConfigReader.getAutoAddSlotsItems().contains(itemStack.getType().name().toUpperCase());
        }
        Material material = itemStack.getType();
        if (material.equals(Material.ENCHANTED_BOOK) || material.equals(Material.BOOK)) {
            return false;
        }
        for (Enchantment enchantment : Enchantment.values()) {
            if (enchantment.canEnchantItem(itemStack)) {
                return true;
            }
        }
        return false;
    }
}
