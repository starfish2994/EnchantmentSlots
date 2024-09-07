package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import cn.superiormc.enchantmentslots.utils.NumberUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.superiormc.enchantmentslots.methods.ItemLimits.ENCHANTMENT_SLOTS_KEY;


public class ItemModify {

    public static String lorePrefix = "";

    public static ItemStack serverToClient(@NotNull ItemStack item, Player player) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return item;
        }
        int slot = ItemLimits.getRealMaxEnchantments(item);
        if (slot == 0) {
            return item;
        }
        List<String> lore = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(item, true);
        if (ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(item)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        String value = TextUtil.parse(CommonUtil.modifyString(ConfigReader.getEnchantPlaceholder()
                                ,"enchant_name", Messages.getEnchantName(item, enchantment, player, true)
                                ,"enchant_raw_name", Messages.getEnchantName(item, enchantment, player, false)
                                ,"enchant_level", getEnchantmentLevel(enchantment, enchantments.get(enchantment))
                                ,"enchant_level_roman", getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment))));
                        value = lorePrefix + value;
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = slot - enchantments.size();
                    while (i > 0) {
                        String value = TextUtil.parse(ConfigReader.getEmptySlotPlaceholder());
                        value = lorePrefix + value;
                        lore.add(value);
                        i--;
                    }
                    continue;
                }
                line = lorePrefix + line;
                lore.add(TextUtil.parse(CommonUtil.modifyString(line,
                        "slot_amount", String.valueOf(slot),
                        "enchant_amount", String.valueOf(enchantments.size()))));

            }
        }
        if (itemMeta.hasLore()) {
            List<String> tempLore = itemMeta.getLore();
            lore.addAll(ConfigReader.editDisplayLore(tempLore, item, player, slot));
        }
        if (!ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(item)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        String value = TextUtil.parse(CommonUtil.modifyString(ConfigReader.getEnchantPlaceholder()
                                        ,"enchant_name", Messages.getEnchantName(item, enchantment, player, true)
                                        ,"enchant_raw_name", Messages.getEnchantName(item, enchantment, player, false)
                                        ,"enchant_level", getEnchantmentLevel(enchantment, enchantments.get(enchantment))
                                        ,"enchant_level_roman", getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment))));
                        value = lorePrefix + value;
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = slot - enchantments.size();
                    while (i > 0) {
                        String value = TextUtil.parse(ConfigReader.getEmptySlotPlaceholder());
                        value = lorePrefix + value;
                        lore.add(value);
                        i--;
                    }
                    continue;
                }
                line = lorePrefix + line;
                lore.add(TextUtil.parse(CommonUtil.modifyString(line,
                        "slot_amount", String.valueOf(slot),
                        "enchant_amount", String.valueOf(enchantments.size()))));

            }
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack clientToServer(@NotNull ItemStack item) {
        int slot = ItemLimits.getRealMaxEnchantments(item);
        if (slot == 0) {
            return item;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta itemMeta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        List<String> newLore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore = itemMeta.getLore();
            for (String str : lore) {
                if (str.startsWith(lorePrefix)) {
                    continue;
                }
                newLore.add(str);
            }
        }
        if (lore.isEmpty() || lore.get(0).isEmpty()) {
            itemMeta.setLore(null);
        } else {
            itemMeta.setLore(newLore);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack removeAndAddLore(ItemStack item, int defaultSlot, String itemID) {
        if (item == null || item.getType().isAir()) {
            return item;
        }
        if (!ItemLimits.canEnchant(item, itemID)) {
            return item;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        meta.getPersistentDataContainer().remove(ENCHANTMENT_SLOTS_KEY);
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY,
                PersistentDataType.INTEGER,
                defaultSlot);
        if (ConfigReader.getAddHideFlag()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, int defaultSlot, String itemID) {
        if (item == null || item.getType().isAir()) {
            return item;
        }
        if (!ItemLimits.canEnchant(item, itemID)) {
            return item;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return item;
        }
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY,
                PersistentDataType.INTEGER,
                defaultSlot);
        if (ConfigReader.getAddHideFlag()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String getEnchantmentLevel(Enchantment enchantment, int level) {
        if (ConfigReader.getEnchantHideOne() && level == enchantment.getMaxLevel() && enchantment.getMaxLevel() == 1) {
            return "";
        }
        return ConfigReader.getEnchantLevel(level);
    }

    public static String getEnchantmentLevelRoman(Enchantment enchantment, int level) {
        if (ConfigReader.getEnchantHideOne() && level == enchantment.getMaxLevel() && enchantment.getMaxLevel() == 1) {
            return "";
        }
        return NumberUtil.convertToRoman(level);
    }
}
