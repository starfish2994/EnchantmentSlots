package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ExtraSlotsItem {

    public static final NamespacedKey ENCHANTMENT_SLOTS_EXTRA = new NamespacedKey(EnchantmentSlots.instance, "enchantment_extra");

    public static ItemStack getExtraSlotItem(String itemID) {
        ItemStack resultItem;
        ConfigurationSection section = EnchantmentSlots.instance.getConfig().getConfigurationSection(
                "add-slot-items." + itemID
        );
        if (section == null) {
            return null;
        }
        resultItem = ItemUtil.buildItemStack(section);
        if (!resultItem.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(resultItem.getType());
            resultItem.setItemMeta(tempMeta);
        }
        ItemMeta meta = resultItem.getItemMeta();
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_EXTRA,
                PersistentDataType.INTEGER,
                section.getInt("add-slots", 1));
        resultItem.setItemMeta(meta);
        return resultItem;
    }

    public static int getExtraSlotItemValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_EXTRA, PersistentDataType.INTEGER)) {
            return 0;
        }
        return meta.getPersistentDataContainer().get(ENCHANTMENT_SLOTS_EXTRA, PersistentDataType.INTEGER);
    }
}
