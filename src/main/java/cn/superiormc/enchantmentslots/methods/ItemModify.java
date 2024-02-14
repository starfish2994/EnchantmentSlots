package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.utils.ColorParser;
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


public class ItemModify {

    public static String lorePrefix = "";

    public static ItemStack serverToClient(@NotNull Player player, @NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        List<String> lore = new ArrayList<>();
        if (ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack) &&
                ItemLimits.getRealMaxEnchantments(player, itemStack) != 0) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                        String value = TextUtil.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", Messages.getEnchantName(enchantment, true)).
                                        replace("{enchant_raw_name}", Messages.getEnchantName(enchantment, false)).
                                        replace("{enchant_level}", String.valueOf(
                                                itemStack.getEnchantments().get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                itemStack.getEnchantments().get(enchantment))));
                        value = lorePrefix + value;
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = ItemLimits.getMaxEnchantments(player, itemStack) - itemStack.getEnchantments().size();
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
                        .replace("{slot_amount}", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                        .replace("{enchant_amount}", String.valueOf(itemStack.getEnchantments().size())));

            }
        }
        if (itemMeta.hasLore()) {
            List<String> tempLore = itemMeta.getLore();
            lore.addAll(ConfigReader.editDisplayLore(tempLore, itemStack, player));
        }
        if (!ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack) &&
                ItemLimits.getRealMaxEnchantments(player, itemStack) != 0) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                        String value = TextUtil.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", Messages.getEnchantName(enchantment, true)).
                                        replace("{enchant_raw_name}", Messages.getEnchantName(enchantment, false)).
                                        replace("{enchant_level}", String.valueOf(
                                                itemStack.getEnchantments().get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                itemStack.getEnchantments().get(enchantment)))).
                                        replace("[enchant_name]", Messages.getEnchantName(enchantment, true)).
                                        replace("[enchant_raw_name]", Messages.getEnchantName(enchantment, false)).
                                        replace("[enchant_level]", String.valueOf(
                                                itemStack.getEnchantments().get(enchantment))).
                                        replace("[enchant_level_roman]", NumberUtil.convertToRoman(
                                                itemStack.getEnchantments().get(enchantment)));
                        value = lorePrefix + value;
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = ItemLimits.getMaxEnchantments(player, itemStack) - itemStack.getEnchantments().size();
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
                        .replace("{slot_amount}", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                        .replace("{enchant_amount}", String.valueOf(itemStack.getEnchantments().size()))
                        .replace("[slot_amount]", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                        .replace("[enchant_amount]", String.valueOf(itemStack.getEnchantments().size())));

            }
        }
        itemMeta.setLore(lore);
        if (ConfigReader.getDebug()) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fAdded lore: " + lore + ".");
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack clientToServer(@NotNull Player player, @NotNull ItemStack itemStack) {
        if (ItemLimits.getRealMaxEnchantments(player, itemStack) == 0) {
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

    public static void addLore(Player player, ItemStack item, boolean fromPacket) {
        if (!ConfigReader.getAutoAddSlotsLimit() && fromPacket) {
            return;
        }
        if (item == null) {
            return;
        }
        if (item.getType() == Material.ENCHANTED_BOOK) {
            return;
        }
        if (ItemLimits.getRealMaxEnchantments(player, item) != 0) {
            return;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (ItemLimits.canEnchant(item)) {
            meta.getPersistentDataContainer().set(ItemLimits.ENCHANTMENT_SLOTS_KEY,
                    PersistentDataType.INTEGER,
                    ConfigReader.getDefaultLimits(player, item));
            item.setItemMeta(meta);
        }
    }
}
