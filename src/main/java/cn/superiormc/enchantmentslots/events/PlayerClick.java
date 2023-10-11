package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            if (event.getSlot() != 2) {
                return;
            }
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player)event.getWhoClicked();
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            ItemStack item = inventory.getItem(0);
            if (item == null) {
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
                        ConfigReader.getDefaultLimits());
                item.setItemMeta(meta);
            }
            ItemStack result = inventory.getItem(2);
            if (result != null) {
                int maxEnchantments = ItemLimits.getMaxEnchantments(result);
                if (result.getEnchantments().size() > maxEnchantments) {
                    event.setCancelled(true);
                    if (ConfigReader.getCloseInventory()) {
                        player.closeInventory();
                    }
                    player.sendMessage(ConfigReader.getMessages("slots-limit-reached"));
                }
            }
        }
    }
}
