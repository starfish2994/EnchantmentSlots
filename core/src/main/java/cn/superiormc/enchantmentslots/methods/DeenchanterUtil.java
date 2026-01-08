package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class DeenchanterUtil {

    public static final NamespacedKey ENCHANTMENT_SLOTS_EXTRA = new NamespacedKey(EnchantmentSlots.instance, "deenchanter_mode");

    public static ItemStack generateCommonDeenchanterItem(Player player, int amount) {
        ItemStack commonItem = ItemUtil.buildItemStack(player,
                ConfigManager.configManager.config.getConfigurationSection("common-deenchanter"));
        ItemMeta itemMeta = commonItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_EXTRA,
                PersistentDataType.BOOLEAN, true);
        commonItem.setItemMeta(itemMeta);
        commonItem.setAmount(amount);
        return commonItem;
    }

    public static ItemStack generateAdvancedDeenchanterItem(Player player, int amount) {
        ItemStack commonItem = ItemUtil.buildItemStack(player,
                ConfigManager.configManager.config.getConfigurationSection("advanced-deenchanter"));
        ItemMeta itemMeta = commonItem.getItemMeta();
        itemMeta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_EXTRA,
                PersistentDataType.BOOLEAN, false);
        commonItem.setItemMeta(itemMeta);
        commonItem.setAmount(amount);
        return commonItem;
    }
}
