package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerEnchant implements Listener {
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        if (!item.hasItemMeta()) {
            ItemMeta tempMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(tempMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (ConfigReader.getAutoAddSlotsLimit()) {
            meta.getPersistentDataContainer().set(ItemLimits.ENCHANTMENT_SLOTS_KEY,
                    PersistentDataType.INTEGER,
                    ConfigReader.getDefaultLimits());
            item.setItemMeta(meta);
        }
        int maxEnchantments = ItemLimits.getMaxEnchantments(item);
        if (event.getEnchantsToAdd().size() + item.getEnchantments().size() > maxEnchantments) {
            event.setCancelled(true);
            if (ConfigReader.getCloseInventory()) {
                player.closeInventory();
            }
            player.sendMessage(ConfigReader.getMessages("slots-limit-reached"));
        }
    }
}
