package cn.superiormc.enchantmentslots.configs;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.Condition;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.*;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConfigReader {

    public static String getLanguage(){
        return EnchantmentSlots.instance.getConfig().getString("language", "en_US");
    }
    public static boolean getDebug() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.debug", false);
    }
    public static boolean getAutoAddLore() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.item-can-be-enchanted.auto-add-lore", true);
    }
    public static String getEnchantLevel(int level) {
        return EnchantmentSlots.instance.getConfig().getString("enchant-level." + level, String.valueOf(level));
    }
    public static String getEnchantmentName(Enchantment enchantment) {
        return EnchantmentSlots.instance.getConfig().getString("enchant-name." + enchantment.getKey().getKey(), enchantment.getKey().getKey());
    }
    public static boolean getCloseInventory() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.close-inventory-if-reached-limit", true);
    }
    public static boolean getOnlyInInventory() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.only-in-player-inventory",
                EnchantmentSlots.instance.getConfig().getBoolean("settings.only-in-player-inventory", true));
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
    public static boolean getEnchantSort() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.placeholder.enchants.sort", true);
    }
    public static boolean getEnchantHideOne() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.placeholder.enchants.level-hide-one", false);
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
        if (!EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.black-item-contains-name").isEmpty()
            && itemStack.getItemMeta().hasDisplayName()) {
            for (String requiredName : EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.black-item-contains-name")) {
                if (itemStack.getItemMeta().getDisplayName().contains(requiredName)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    public static boolean getAddHideFlag() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.add-hide-enchant-flag", false);
    }
    public static boolean getSmithItemTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SmithItemEvent.enabled", true);
    }
    public static boolean getSmithItemGreater() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SmithItemEvent.keep-greater-slot", true);
    }
    public static boolean getSmithItemReset() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SmithItemEvent.reset-previous-slot", true);
    }
    public static boolean getEnchantItemTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.EnchantItemEvent.enabled", true);
    }
    public static boolean getEnchantCancel() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.EnchantItemEvent.cancel-if-reached-slot", true);
    }
    public static boolean getAnvilItemTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.AnvilItemEvent.enabled", true);
    }
    public static boolean getSetSlotPacketTrigger() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SetSlotPacket.enabled",
                EnchantmentSlots.instance.getConfig().getBoolean("settings.add-lore.trigger.Packet.enabled", false));
    }
    public static boolean getRemoveExtraEnchants() {
        return EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SetSlotPacket.remove-illegal-excess-enchant",
                EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.SetSlotPacket.remove-illegal-extra-enchant", false));
    }
    public static List<String> getItemCanBeEnchantedWhiteList() {
        if (EnchantmentSlots.instance.getConfig().getStringList("settings.item-can-be-enchanted.whitelist-items").isEmpty()) {
            return EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.trigger.Packet.whitelist-items");
        }
        return EnchantmentSlots.instance.getConfig().getStringList("settings.item-can-be-enchanted.whitelist-items");
    }
    public static List<String> getItemCanBeEnchantedBlackList() {
        return EnchantmentSlots.instance.getConfig().getStringList("settings.item-can-be-enchanted.blacklist-items");
    }
    public static List<String> getDisplayLore() {
        return EnchantmentSlots.instance.getConfig().getStringList("settings.add-lore.display-value");
    }
    public static List<String> editDisplayLore(List<String> lore, ItemStack itemStack, Player player, int slot) {
        List<String> tempLore = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = ItemUtil.getEnchantments(itemStack, true);
        for (String str : lore) {
            if (str.contains("{enchants}")) {
                for (Enchantment enchantment : enchantments.keySet()) {
                    tempLore.add(TextUtil.parse(
                            ConfigReader.getEnchantPlaceholder().
                                    replace("{enchant_name}", Messages.getEnchantName(itemStack, enchantment, player, true)).
                                    replace("{enchant_raw_name}", Messages.getEnchantName(itemStack, enchantment, player, false)).
                                    replace("{enchant_level}", ItemModify.getEnchantmentLevel(enchantment, enchantments.get(enchantment))).
                                    replace("{enchant_level_roman}", ItemModify.getEnchantmentLevelRoman(enchantment, enchantments.get(enchantment)))));
                }
                continue;
            }
            if (str.contains("{empty_slots}")) {
                int i = slot - enchantments.size();
                while (i > 0) {
                    tempLore.add(TextUtil.parse(ConfigReader.getEmptySlotPlaceholder()));
                    i--;
                }
                continue;
            }
            tempLore.add(str
                    .replace("{slot_amount}", String.valueOf(slot))
                    .replace("{enchant_amount}", String.valueOf(enchantments.size())));
        }
        return tempLore;
    }
    public static int getDefaultLimits(Player player, String itemID) {
        ConfigurationSection section = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.default-slots-by-item." + itemID);
        if (ConfigReader.getDebug()) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fItem ID: " + itemID);
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
            if (groupName.equals("default") || (section.getInt(groupName, -1) != -1 &&
                    Condition.getBoolean(player, conditionSection.getStringList(groupName)))) {
                result.add(section.getInt(groupName));
            }
        }
        if (result.isEmpty()) {
            if (section.getInt("default", -1) == -1) {
                result.add(5);
            } else {
                result.add(section.getInt("default"));
            }
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
            if (groupName.equals("default") || (section.getInt(groupName, -1) != -1 &&
                    Condition.getBoolean(player, conditionSection.getStringList(groupName)))) {
                result.add(section.getInt(groupName));
            }
        }
        if (result.isEmpty()) {
            result.add(section.getInt("default", -1));
        }
        return Collections.max(result);
    }
    public static ListenerPriority getPriority() {
        return ListenerPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.add-lore.packet-listener-priority", EnchantmentSlots.instance.getConfig().getString(
                        "settings.packet-listener-priority", "MONITOR")).toUpperCase());
    }
    public static DisplayPriority getEcoPriority() {
        return DisplayPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.add-lore.packet-listener-priority", EnchantmentSlots.instance.getConfig().getString(
                        "settings.packet-listener-priority", "HIGHEST")).toUpperCase());
    }
}
