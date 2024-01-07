package cn.superiormc.enchantmentslots.configs;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.utils.*;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ConfigReader {

    public static boolean getDebug() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.debug", false);
    }
    public static String getEnchantmentName(Enchantment enchantment) {
        return EnchantmentSlots.instance.getConfig().getString("enchant-name." + enchantment.getKey().getKey(), enchantment.getKey().getKey());
    }
    public static boolean getCloseInventory() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.close-inventory-if-reached-limit", true);
    }
    public static boolean getRemoveExtraEnchants() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.remove-illegal-extra-enchant", true);
    }
    public static boolean getCancelMaxLimits() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.cancel-add-slot-if-reached-max-slot", true);
    }
    public static boolean getUseTiers() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.use-tier-identify-slots", false);
    }
    public static boolean getAtFirstOrLast() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.at-first-or-last", false);
    }
    public static String getEnchantPlaceholder() {
        return EnchantmentSlots.instance.getConfig().getString("settings.add-lore.placeholder.enchants.format",
                "&6  {enchant_name}");
    }
    public static String getEmptySlotPlaceholder() {
        return EnchantmentSlots.instance.getConfig().getString("settings.add-lore.placeholder.empty-slots.format",
                "&7  --- Empty Slot ---");
    }
    public static boolean getBlackItems(ItemStack itemStack) {
        List<String> tempVal1 = new ArrayList<>();
        for (String tempVal2 : EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.black-items")) {
            tempVal1.add(tempVal2.toLowerCase());
        }
        if (tempVal1.contains(itemStack.getType().getKey().getKey())) {
            return true;
        }
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        if (EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.black-item-has-lore", false) &&
                itemStack.getItemMeta().hasLore()) {
            return true;
        }
        if (!EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.black-item-contains-lore").isEmpty()
        && itemStack.getItemMeta().hasLore()) {
            for (String hasLore : itemStack.getItemMeta().getLore()) {
                for (String requiredLore : EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.black-item-contains-lore")) {
                    if (hasLore.contains(requiredLore)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    public static boolean getEnchantItemTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.trigger.EnchantItemEvent.enabled", true);
    }
    public static boolean getInventoryClickTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.trigger.InventoryClickEvent.enabled", true);
    }
    public static boolean getAutoAddSlotsLimit() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.trigger.Packet.enabled", true);
    }
    public static boolean getAutoAddSlotsAutoCheck() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.item-can-be-enchanted.auto-check", true);
    }
    public static List<String> getAutoAddSlotsItems() {
        if (EnchantmentSlots.instance.getConfig().getStringList("settings.item-can-be-enchanted.whitelist-items").isEmpty()) {
            return EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.trigger.Packet.whitelist-items");
        }
        return EnchantmentSlots.instance.getConfig().getStringList("settings.item-can-be-enchanted.whitelist-items");
    }
    public static List<String> getDisplayLore() {
        return EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.display-value");
    }
    public static List<String> editDisplayLore(List<String> lore, ItemStack itemStack, Player player) {
        List<String> tempLore = new ArrayList<>();
        for (String str : lore) {
            if (str.contains("{enchants}")) {
                for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                    tempLore.add(ColorParser.parse(
                            ConfigReader.getEnchantPlaceholder().
                                    replace("{enchant_name}", Messages.getEnchantName(enchantment, true)).
                                    replace("{enchant_raw_name}", Messages.getEnchantName(enchantment, false)).
                                    replace("{enchant_level}", String.valueOf(
                                            itemStack.getEnchantments().get(enchantment))).
                                    replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                            itemStack.getEnchantments().get(enchantment)))));
                }
                continue;
            }
            if (str.contains("{empty_slots}")) {
                int i = ItemLimits.getMaxEnchantments(player, itemStack) - itemStack.getEnchantments().size();
                while (i > 0) {
                    tempLore.add(ColorParser.parse(ConfigReader.getEmptySlotPlaceholder()));
                    i--;
                }
                continue;
            }
            tempLore.add(str
                    .replace("{slot_amount}", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                    .replace("{enchant_amount}", String.valueOf(itemStack.getEnchantments().size())));
        }
        return tempLore;
    }
    public static int getDefaultLimits(Player player, ItemStack itemStack) {
        ConfigurationSection section = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.default-slots-by-item." +
                        CheckValidHook.checkValid(itemStack));
        if (ConfigReader.getDebug()) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] Item ID: " + CheckValidHook.checkValid(itemStack));
        }
        if (section == null) {
            section = EnchantmentSlots.instance.getConfig().
                    getConfigurationSection("settings.default-slots");
        }
        ConfigurationSection conditionSection = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.slots-conditions");
        if (section == null) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cError: " +
                    "Can not found default-slots section, default set to 5!");
            return 5;
        }
        if (conditionSection == null) {
            return section.getInt("default", 5);
        }
        Set<String> groupNameSet = conditionSection.getKeys(false);
        List<Integer> result = new ArrayList<>();
        for (String groupName : groupNameSet) {
            if (section.getInt(groupName, 0) != 0 && Condition.getBoolean(player, conditionSection.getStringList(groupName))) {
                result.add(section.getInt(groupName));
            }
            else {
                if (section.getInt("default", 0) != 0) {
                    result.add(section.getInt("default", 5));
                }
            }
        }
        if (result.isEmpty()) {
            result.add(5);
        }
        return Collections.max(result);
    }
    public static int getMaxLimits(Player player, ItemStack itemStack) {
        ConfigurationSection section = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.max-slots-by-item." +
                        CheckValidHook.checkValid(itemStack));
        if (section == null) {
            section = EnchantmentSlots.instance.getConfig().
                    getConfigurationSection("settings.max-slots");
        }
        ConfigurationSection conditionSection = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.slots-conditions");
        if (section == null) {
            return -1;
        }
        if (conditionSection == null) {
            return section.getInt("default", -1);
        }
        Set<String> groupNameSet = conditionSection.getKeys(false);
        List<Integer> result = new ArrayList<>();
        for (String groupName : groupNameSet) {
            if (section.getInt(groupName, 0) != 0 && Condition.getBoolean(player, conditionSection.getStringList(groupName))) {
                result.add(section.getInt(groupName));
            }
            else {
                if (section.getInt("default") != 0) {
                    result.add(section.getInt("default", -1));
                }
            }
        }
        if (result.isEmpty()) {
            result.add(-1);
        }
        return Collections.max(result);
    }
    public static ListenerPriority getPriority() {
        return ListenerPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.packet-listener-priority", "MONITOR").toUpperCase());
    }
    public static DisplayPriority getEcoPriority() {
        return DisplayPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.packet-listener-priority", "HIGHEST").toUpperCase());
    }
}
