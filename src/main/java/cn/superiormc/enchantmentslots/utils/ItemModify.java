package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemModify {

    public static ItemStack serverToClient(ItemStack serverItemStack) {
        if (ItemLimits.getMaxEnchantments(serverItemStack) == 0) {
            return serverItemStack;
        }
        ItemStack clientItemStack = serverItemStack.clone();
        if (!clientItemStack.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(clientItemStack.getType());
            clientItemStack.setItemMeta(tempMeta);
        }
        ItemMeta itemMeta = clientItemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            lore = itemMeta.getLore();
        }
        lore.add(ColorParser.parse(ConfigReader.getDisplayLore()
                .replace("%amount%", String.valueOf(ItemLimits.getMaxEnchantments(serverItemStack)))));
        itemMeta.setLore(lore);
        if (ConfigReader.getDebug()) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fAdded lore: " + lore + ".");
        }
        clientItemStack.setItemMeta(itemMeta);
        return clientItemStack;
    }

    public static ItemStack clientToServer(ItemStack clientItemStack) {
        if (ItemLimits.getMaxEnchantments(clientItemStack) == 0) {
            return clientItemStack;
        }
        ItemStack serverItemStack = clientItemStack.clone();
        if (!serverItemStack.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(serverItemStack.getType());
            serverItemStack.setItemMeta(tempMeta);
        }
        ItemMeta itemMeta = serverItemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            List<String> newLore = new ArrayList<>();
            lore = itemMeta.getLore();
            for (String str : lore) {
                if (!str.contains(ConfigReader.getDisplayLoreContains())) {
                    newLore.add(str);
                    if (ConfigReader.getDebug()) {
                        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fRemoved lore:" + str);
                    }
                }
            }
            lore = newLore;
        }
        if (lore.isEmpty()) {
            itemMeta.setLore(null);
        }
        else {
            itemMeta.setLore(lore);
        }
        serverItemStack.setItemMeta(itemMeta);
        return serverItemStack;
    }

    public static void addLore(Player player, ItemStack item, boolean fromPacket) {
        if (!ConfigReader.getAutoAddSlotsLimit() && fromPacket) {
            return;
        }
        if (item == null) {
            return;
        }
        if (ItemLimits.getMaxEnchantments(item) != 0) {
            return;
        }
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (ConfigReader.getAutoAddSlotsLimit()) {
            meta.getPersistentDataContainer().set(ItemLimits.ENCHANTMENT_SLOTS_KEY,
                    PersistentDataType.INTEGER,
                    ConfigReader.getDefaultLimits(player));
            item.setItemMeta(meta);
        }
    }
}
