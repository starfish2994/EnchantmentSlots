package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SlotUtil {

    public static final NamespacedKey ENCHANTMENT_SLOTS_KEY = new NamespacedKey(EnchantmentSlots.instance, "enchantment_slots");

    public static ItemStack setSlot(ItemStack item, Player player, boolean override) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (override) {
            meta.getPersistentDataContainer().remove(ENCHANTMENT_SLOTS_KEY);
        }
        if (meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return item;
        }
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(item, player);
        if (defaultSlot > 0) {
            meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY,
                    PersistentDataType.INTEGER, defaultSlot);
        }
        if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.add-hide-enchant-flag", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setSlot(ItemStack item, int slotValue, boolean override) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (override) {
            meta.getPersistentDataContainer().remove(ENCHANTMENT_SLOTS_KEY);
        }
        if (meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return item;
        }
        if (slotValue > 0) {
            meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY,
                    PersistentDataType.INTEGER,
                    slotValue);
        }
        if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.add-hide-enchant-flag", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static int getSlot(ItemStack item) {
        if (!item.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return 0;
        }
        return meta.getPersistentDataContainer().get(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER);
    }

    public static void removeSlot(ItemStack item) {
        ItemMeta meta =item.getItemMeta();
        if (meta == null) {
            return;
        }
        meta.getPersistentDataContainer().remove(ENCHANTMENT_SLOTS_KEY);
    }

}
