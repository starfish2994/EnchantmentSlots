package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import com.google.common.base.Enums;
import com.google.common.collect.MultimapBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemUtil {
    
    public static ItemStack buildItemStack(ConfigurationSection section) {
        ItemStack item = new ItemStack(Material.STONE);
        String materialKey = section.getString("material");
        if (materialKey != null) {
            Material material = Material.getMaterial(materialKey.toUpperCase());
            if (material != null) {
                item.setType(material);
            }
        }
        int amountKey = section.getInt("amount", -1);
        if (amountKey > 0) {
            item.setAmount(amountKey);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }

        String displayNameKey = section.getString("name");
        if (displayNameKey != null) {
            EnchantmentSlots.methodUtil.setItemName(meta, displayNameKey);
        }
        List<String> loreKey = section.getStringList("lore");
        if (!loreKey.isEmpty()) {
            EnchantmentSlots.methodUtil.setItemLore(meta, loreKey);
        }
        if (CommonUtil.getMajorVersion(14)) {
            int customModelDataKey = section.getInt("custom-model-data", section.getInt("cmd", -1));
            if (customModelDataKey > 0) {
                meta.setCustomModelData(customModelDataKey);
            }
        }
        List<String> itemFlagKey = section.getStringList("flags");
        if (!itemFlagKey.isEmpty()) {
            for (String flag : itemFlagKey) {
                flag = flag.toUpperCase();
                ItemFlag itemFlag = Enums.getIfPresent(ItemFlag.class, flag).orNull();
                if (itemFlag != null) {
                    meta.addItemFlags(itemFlag);
                }
                if (CommonUtil.getMinorVersion(20, 6) && itemFlag == ItemFlag.HIDE_ATTRIBUTES && meta.getAttributeModifiers() == null) {
                    meta.setAttributeModifiers(MultimapBuilder.hashKeys().hashSetValues().build());
                }
            }
        }
        ConfigurationSection enchantsKey = section.getConfigurationSection("enchants");
        if (enchantsKey != null) {
            for (String ench : enchantsKey.getKeys(false)) {
                Enchantment vanillaEnchant = Enchantment.getByKey(NamespacedKey.minecraft(ench.toLowerCase()));
                if (vanillaEnchant != null) {
                    meta.addEnchant(vanillaEnchant, enchantsKey.getInt(ench), true);
                }
            }
        }
        // Skull
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            String skullTextureNameKey = section.getString("skull-meta", section.getString("skull"));
            if (skullTextureNameKey != null) {
                if (skullTextureNameKey.length() > 16) {
                    EnchantmentSlots.methodUtil.setSkullMeta(skullMeta, skullTextureNameKey);
                } else {
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullTextureNameKey));
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isValid(ItemStack item) {
        return item != null && !item.getType().isAir();
    }
}
