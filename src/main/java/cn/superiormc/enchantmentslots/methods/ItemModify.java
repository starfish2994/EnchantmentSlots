package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import cn.superiormc.enchantmentslots.utils.NumberUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
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

    public static ItemStack serverToClient(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        int slot = ItemLimits.getRealMaxEnchantments(itemStack);
        if (slot == 0) {
            return itemStack;
        }
        List<String> lore = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(itemStack, true);
        if (ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        String value = TextUtil.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", Messages.getEnchantName(itemStack, enchantment, true)).
                                        replace("{enchant_raw_name}", Messages.getEnchantName(itemStack, enchantment, false)).
                                        replace("{enchant_level}", String.valueOf(
                                                enchantments.get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                enchantments.get(enchantment)))).
                                        replace("[enchant_name]", Messages.getEnchantName(itemStack, enchantment, true)).
                                        replace("[enchant_raw_name]", Messages.getEnchantName(itemStack, enchantment, false)).
                                        replace("[enchant_level]", String.valueOf(
                                                enchantments.get(enchantment))).
                                        replace("[enchant_level_roman]", NumberUtil.convertToRoman(
                                                enchantments.get(enchantment)));
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
                lore.add(TextUtil.parse(line)
                        .replace("{slot_amount}", String.valueOf(slot))
                        .replace("{enchant_amount}", String.valueOf(enchantments.size())));

            }
        }
        if (itemMeta.hasLore()) {
            List<String> tempLore = itemMeta.getLore();
            lore.addAll(ConfigReader.editDisplayLore(tempLore, itemStack, slot));
        }
        if (!ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        String value = TextUtil.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", Messages.getEnchantName(itemStack, enchantment, true)).
                                        replace("{enchant_raw_name}", Messages.getEnchantName(itemStack, enchantment, false)).
                                        replace("{enchant_level}", String.valueOf(
                                                enchantments.get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                enchantments.get(enchantment)))).
                                        replace("[enchant_name]", Messages.getEnchantName(itemStack, enchantment, true)).
                                        replace("[enchant_raw_name]", Messages.getEnchantName(itemStack, enchantment, false)).
                                        replace("[enchant_level]", String.valueOf(
                                                enchantments.get(enchantment))).
                                        replace("[enchant_level_roman]", NumberUtil.convertToRoman(
                                                enchantments.get(enchantment)));
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
                lore.add(TextUtil.parse(line)
                        .replace("{slot_amount}", String.valueOf(slot))
                        .replace("{enchant_amount}", String.valueOf(enchantments.size()))
                        .replace("[slot_amount]", String.valueOf(slot))
                        .replace("[enchant_amount]", String.valueOf(enchantments.size())));

            }
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack clientToServer(@NotNull ItemStack itemStack) {
        int slot = ItemLimits.getRealMaxEnchantments(itemStack);
        if (slot == 0) {
            return itemStack;
        }
        if (!itemStack.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
            itemStack.setItemMeta(tempMeta);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
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
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void addLore(ItemStack item, int defaultSlot, String itemID) {
        if (item == null || item.getType().isAir()) {
            return;
        }
        if (!ItemLimits.canEnchant(item, itemID)) {
            return;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        if (meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_KEY, PersistentDataType.INTEGER)) {
            return;
        }
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_KEY,
                PersistentDataType.INTEGER,
                defaultSlot);
        if (ConfigReader.getAddHideFlag()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
    }
}
