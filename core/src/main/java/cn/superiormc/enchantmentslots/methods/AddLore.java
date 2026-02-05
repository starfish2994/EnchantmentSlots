package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.managers.LicenseManager;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
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
        if (!LicenseManager.licenseManager.valid) {
            return item;
        }
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
            itemLore = EnchantmentSlots.methodUtil.getItemLore(meta);
        } else {
            itemLore = new ArrayList<>();
        }
        if (ConfigManager.configManager.getBoolean("settings.add-lore.remove-lore-first", true)) {
            itemLore.removeIf(tempVal1 -> tempVal1.contains(lorePrefix));
        }
        int index = 0;
        if (!ConfigManager.configManager.getBoolean("settings.add-lore.at-first-or-last", false)) {
            index = itemLore.size();
        }
        Map<Enchantment, Integer> enchantments = EnchantsUtil.getEnchantments(item, true);
        for (String line : ConfigManager.configManager.getStringList(player, "settings.add-lore.display-value")) {
            if (line.equals("{enchants}")) {
                for (Enchantment enchantment : enchantments.keySet()) {
                    // Without color code.
                    String value = ConfigManager.configManager.getString(player, "settings.add-lore.placeholder.enchants.format",
                            "&6  {enchant_name}"
                            ,"enchant_name", HookManager.hookManager.getEnchantName(item, enchantment, player, true)
                            ,"enchant_raw_name", HookManager.hookManager.getEnchantName(item, enchantment, player, false)
                            ,"enchant_level", EnchantsUtil.getEnchantmentLevel(enchantment, enchantments.get(enchantment), player)
                            ,"enchant_level_roman", EnchantsUtil.getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment))
                            ,"enchant_used_slot", EnchantsUtil.getUsedSlotPlaceholder(enchantment, player));
                    value = lorePrefix + value;
                    itemLore.add(index, value);
                    index++;
                }
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                continue;
            }
            if (line.equals("{empty_slots}")) {
                int i = slot - EnchantsUtil.getUsedSlot(enchantments.keySet());
                while (i > 0) {
                    String value = ConfigManager.configManager.getString(player, "settings.add-lore.placeholder.empty-slots.format",
                            "&7  --- Empty Slot ---");
                    value = lorePrefix + value;
                    itemLore.add(index, value);
                    index++;
                    i--;
                }
                continue;
            }
            line = lorePrefix + line;
            itemLore.add(index, CommonUtil.modifyString(player, line,
                    "slot_amount", String.valueOf(slot),
                    "enchant_amount", String.valueOf(enchantments.size()),
                    "used_slot_amount", String.valueOf(EnchantsUtil.getUsedSlot(enchantments.keySet()))));
            index++;

        }
        EnchantmentSlots.methodUtil.setItemLore(meta, itemLore, player);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack removeLore(ItemStack item, Player player) {
        List<String> itemLore;
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            itemLore = EnchantmentSlots.methodUtil.getItemLore(meta);
        } else {
            itemLore = new ArrayList<>();
        }
        itemLore.removeIf(tempVal1 -> tempVal1.contains(lorePrefix));
        EnchantmentSlots.methodUtil.setItemLore(meta, itemLore, player);
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
        if (!LicenseManager.licenseManager.valid) {
            return meta;
        }
        List<String> itemLore = new ArrayList<>();
        List<String> newLore = new ArrayList<>();
        if (meta.hasLore()) {
            itemLore = EnchantmentSlots.methodUtil.getItemLore(meta);
        }
        int slot = SlotUtil.getSlot(meta);
        Map<Enchantment, Integer> enchantments = EnchantsUtil.getEnchantments(meta, true);
        if (itemLore != null) {
            for (String str : itemLore) {
                if (str.contains("{enchants}")) {
                    for (Enchantment enchantment : enchantments.keySet()) {
                        newLore.add(ConfigManager.configManager.getString(player, "settings.add-lore.placeholder.enchants.format",
                                        "&6  {enchant_name}",
                                        "enchant_name", HookManager.hookManager.getEnchantName(item, enchantment, player, true),
                                        "enchant_raw_name", HookManager.hookManager.getEnchantName(item, enchantment, player, false),
                                        "enchant_level", EnchantsUtil.getEnchantmentLevel(enchantment, enchantments.get(enchantment), player),
                                        "enchant_level_roman", EnchantsUtil.getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment)),
                                        "enchant_used_slot", EnchantsUtil.getUsedSlotPlaceholder(enchantment, player)));
                    }
                    continue;
                }
                if (str.contains("{empty_slots}")) {
                    int i = slot - EnchantsUtil.getUsedSlot(enchantments.keySet());
                    while (i > 0) {
                        newLore.add(ConfigManager.configManager.getString(player, "settings.add-lore.placeholder.empty-slots.format",
                                "&7  --- Empty Slot ---"));
                        i--;
                    }
                    continue;
                }
                newLore.add(CommonUtil.modifyString(player, str,
                        "slot_amount", String.valueOf(slot),
                        "enchant_amount", String.valueOf(enchantments.size()),
                        "used_slot_amount", String.valueOf(EnchantsUtil.getUsedSlot(enchantments.keySet()))));
            }
        }
        EnchantmentSlots.methodUtil.setItemLore(meta, newLore, player);
        return meta;
    }
}
