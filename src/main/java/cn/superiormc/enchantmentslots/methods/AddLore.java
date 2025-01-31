package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddLore {

    public static String lorePrefix;

    public static ItemStack autoAddLore(ItemStack item, Player player, boolean inPlayerInventory) {
        if (ConfigManager.configManager.isAutoAddLore(item, player, inPlayerInventory)) {
            SlotUtil.setSlot(item, player, false);
        }
        return addLore(item, player);
    }

    public static ItemStack addLore(ItemStack item, Player player) {
        List<String> itemLore;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        int slot = SlotUtil.getSlot(meta);
        if (slot == 0) {
            return item;
        }
        if (ConfigManager.configManager.getBoolean("settings.add-lore.placeholder.auto-parse", true)) {
            item.setItemMeta(AddLore.parseLore(item, meta, player));
        }
        if (!ConfigManager.configManager.canDisplay(item)) {
            return item;
        }
        if (meta.hasLore()) {
            itemLore = meta.getLore();
        } else {
            itemLore = new ArrayList<>();
        }
        if (ConfigManager.configManager.getBoolean("settings.add-lore.remove-lore-first", true)) {
            itemLore.removeIf(tempVal1 -> tempVal1.startsWith(lorePrefix));
        }
        int index = 0;
        if (!ConfigManager.configManager.getBoolean("settings.add-lore.at-first-or-last", false)) {
            index = itemLore.size();
        }
        Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(item, true);
        for (String line : ConfigManager.configManager.getStringList("settings.add-lore.display-value")) {
            if (line.equals("{enchants}")) {
                for (Enchantment enchantment : enchantments.keySet()) {
                    String value = TextUtil.parse(ConfigManager.configManager.getString("settings.add-lore.placeholder.enchants.format",
                            "&6  {enchant_name}"
                            ,"enchant_name", LanguageManager.languageManager.getEnchantName(item, enchantment, player, true)
                            ,"enchant_raw_name", LanguageManager.languageManager.getEnchantName(item, enchantment, player, false)
                            ,"enchant_level", ItemUtil.getEnchantmentLevel(enchantment, enchantments.get(enchantment))
                            ,"enchant_level_roman", ItemUtil.getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment))));
                    value = lorePrefix + value;
                    itemLore.add(index, value);
                    index++;
                }
                continue;
            }
            if (line.equals("{empty_slots}")) {
                int i = slot - enchantments.size();
                while (i > 0) {
                    String value = TextUtil.parse(ConfigManager.configManager.getString("settings.add-lore.placeholder.empty-slots.format",
                            "&7  --- Empty Slot ---"));
                    value = lorePrefix + value;
                    itemLore.add(index, value);
                    index++;
                    i--;
                }
                continue;
            }
            line = lorePrefix + line;
            itemLore.add(index, TextUtil.parse(CommonUtil.modifyString(line,
                    "slot_amount", String.valueOf(slot),
                    "enchant_amount", String.valueOf(enchantments.size()))));
            index++;

        }
        if (ConfigManager.configManager.getBoolean("settings.add-lore.auto-hide-enchants", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeLore(ItemStack item) {
        List<String> itemLore;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            itemLore = meta.getLore();
        } else {
            itemLore = new ArrayList<>();
        }
        itemLore.removeIf(tempVal1 -> tempVal1.startsWith(lorePrefix));
        meta.setLore(itemLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack parseLore(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        item.setItemMeta(parseLore(item, meta, player));
        return item;
    }

    public static ItemMeta parseLore(ItemStack item, ItemMeta meta, Player player) {
        List<String> itemLore = new ArrayList<>();
        List<String> newLore = new ArrayList<>();
        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }
        int slot = SlotUtil.getSlot(meta);
        Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(meta, true);
        if (itemLore != null) {
            for (String str : itemLore) {
                if (str.contains("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        newLore.add(TextUtil.parse(
                                ConfigManager.configManager.getString("settings.add-lore.placeholder.enchants.format",
                                        "&6  {enchant_name}",
                                        "enchant_name", LanguageManager.languageManager.getEnchantName(item, enchantment, player, true),
                                        "enchant_raw_name", LanguageManager.languageManager.getEnchantName(item, enchantment, player, false),
                                        "enchant_level", ItemUtil.getEnchantmentLevel(enchantment, enchantments.get(enchantment)),
                                        "enchant_level_roman", ItemUtil.getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment)))));
                    }
                    continue;
                }
                if (str.contains("{empty_slots}")) {
                    int i = slot - enchantments.size();
                    while (i > 0) {
                        newLore.add(TextUtil.parse(ConfigManager.configManager.getString("settings.add-lore.placeholder.empty-slots.format",
                                "&7  --- Empty Slot ---")));
                        i--;
                    }
                    continue;
                }
                newLore.add(CommonUtil.modifyString(str, "slot_amount", String.valueOf(slot),
                        "enchant_amount", String.valueOf(enchantments.size())));
            }
        }
        meta.setLore(newLore);
        return meta;
    }
}
