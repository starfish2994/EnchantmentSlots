package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        lore.add(ConfigReader.getMessages("add-lore")
                .replace("%amount%", String.valueOf(ItemLimits.getMaxEnchantments(serverItemStack))));
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
            lore = itemMeta.getLore();
            if (lore.remove(ConfigReader.getMessages("add-lore")
                    .replace("%amount%", String.valueOf(
                            ItemLimits.getMaxEnchantments(clientItemStack))))) {
                if (ConfigReader.getDebug()) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fRemoved lore.");
                }
            }
        }
        itemMeta.setLore(lore);
        return serverItemStack;
    }
}
