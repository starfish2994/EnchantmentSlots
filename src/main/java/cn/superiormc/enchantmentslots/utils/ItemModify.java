package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.protolcol.GeneralProtolcol;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.GeneralPackets;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentenchants.enchantment.impl.ExcellentEnchant;
import su.nightexpress.excellentenchants.enchantment.registry.EnchantRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ItemModify {

    public static ItemStack serverToClient(@NotNull Player player, @NotNull ItemStack itemStack) {
        if (ItemLimits.getRealMaxEnchantments(player, itemStack) == 0) {
            return itemStack;
        }
        if (!itemStack.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
            itemStack.setItemMeta(tempMeta);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                        String value = ColorParser.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", getEnchantName(enchantment)).
                                        replace("{enchant_level}", String.valueOf(
                                                itemStack.getEnchantments().get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                itemStack.getEnchantments().get(enchantment))));
                        if (GeneralProtolcol.plugin.equals("eco")) {
                            value = "§z" + value;
                        }
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = ItemLimits.getMaxEnchantments(player, itemStack) - itemStack.getEnchantments().size();
                    while (i > 0) {
                        String value = ColorParser.parse(ConfigReader.getEmptySlotPlaceholder());
                        if (GeneralProtolcol.plugin.equals("eco")) {
                            value = "§z" + value;
                        }
                        lore.add(value);
                        i--;
                    }
                    continue;
                }
                if (GeneralProtolcol.plugin.equals("eco")) {
                    line = "§z" + line;
                }
                lore.add(ColorParser.parse(line)
                        .replace("{slot_amount}", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                        .replace("{enchant_amount}", String.valueOf(itemStack.getEnchantments().size())));

            }
        }
        if (itemMeta.hasLore()) {
            List<String> tempLore = itemMeta.getLore();
            lore.addAll(ConfigReader.editDisplayLore(tempLore, itemStack, player));
        }
        if (!ConfigReader.getAtFirstOrLast() && !ConfigReader.getBlackItems(itemStack)) {
            for (String line : ConfigReader.getDisplayLore()) {
                if (line.equals("{enchants}")) {
                    for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                        String value = ColorParser.parse(
                                ConfigReader.getEnchantPlaceholder().
                                        replace("{enchant_name}", getEnchantName(enchantment)).
                                        replace("{enchant_level}", String.valueOf(
                                                itemStack.getEnchantments().get(enchantment))).
                                        replace("{enchant_level_roman}", NumberUtil.convertToRoman(
                                                itemStack.getEnchantments().get(enchantment))));
                        if (GeneralProtolcol.plugin.equals("eco")) {
                            value = "§z" + value;
                        }
                        lore.add(value);
                    }
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    continue;
                }
                if (line.equals("{empty_slots}")) {
                    int i = ItemLimits.getMaxEnchantments(player, itemStack) - itemStack.getEnchantments().size();
                    while (i > 0) {
                        String value = ColorParser.parse(ConfigReader.getEmptySlotPlaceholder());
                        if (GeneralProtolcol.plugin.equals("eco")) {
                            value = "§z" + value;
                        }
                        lore.add(value);
                        i--;
                    }
                    continue;
                }
                if (GeneralProtolcol.plugin.equals("eco")) {
                    line = "§z" + line;
                }
                lore.add(ColorParser.parse(line)
                        .replace("{slot_amount}", String.valueOf(ItemLimits.getMaxEnchantments(player, itemStack)))
                        .replace("{enchant_amount}", String.valueOf(itemStack.getEnchantments().size())));

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
            bigfor:
            for (String str : lore) {
                Pattern pattern2 = Pattern.compile(ColorParser.parse(
                                ConfigReader.getEnchantPlaceholder()).
                        replace("{enchant_name}", "(.*)").
                        replace("{enchant_level}", "(\\d+)").
                        replace("{enchant_level_roman}", "(.*)"),
                        Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = pattern2.matcher(str);
                if (matcher2.find()) {
                    for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                        if (matcher2.group().equals(getEnchantName(enchantment))) {
                            continue bigfor;
                        }
                    }
                }
                if (str.equalsIgnoreCase(ColorParser.parse(ConfigReader.getEmptySlotPlaceholder()))) {
                    continue;
                }
                for (String configStr : ConfigReader.getDisplayLore()) {
                    if (!configStr.equals("{enchants}") && !configStr.equals("{empty_slots}")) {
                        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
                        Pattern pattern1 = Pattern.compile(ColorParser.parse(configStr)
                                .replace("{slot_amount}", "(\\d+)")
                                .replace("{enchant_amount}", "(\\d+)"), Pattern.CASE_INSENSITIVE);
                        Matcher matcher1 = pattern1.matcher(str);
                        if (matcher1.find()) {
                            continue bigfor;
                        }
                    }
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

    public static String getEnchantName(Enchantment enchantment) {
        if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("EcoEnchants")) {
            if (EnchantmentSlots.instance.getServer().getPluginManager().getPlugin("EcoEnchants").getDescription().
                    getVersion().startsWith("11")) {
                EcoEnchant ecoEnchant = EcoEnchants.INSTANCE.getByID(enchantment.getKey().getKey());
                if (ecoEnchant != null) {
                    return ecoEnchant.getRawDisplayName();
                }
            }
            else {
                com.willfp.ecoenchants.enchants.EcoEnchant ecoEnchant = com.willfp.ecoenchants.enchants.EcoEnchants.getByKey(enchantment.getKey());
                if (ConfigReader.getDebug()) {
                    Bukkit.getConsoleSender().sendMessage(com.willfp.ecoenchants.enchants.EcoEnchants.keySet() + "");
                }
                if (ecoEnchant != null) {
                    return ecoEnchant.getDisplayName();
                }
            }
        } else if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("ExcellentEnchants")) {
            ExcellentEnchant excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
            if (excellentEnchant != null) {
                return excellentEnchant.getDisplayName();
            }
        }
        return ConfigReader.getEnchantmentName(enchantment);
    }
}
