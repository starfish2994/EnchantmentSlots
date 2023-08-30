package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerAnvil {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            ItemStack item = inventory.getItem(0);
            ItemStack result = inventory.getItem(2);
            if (item != null && result != null) {
                int maxEnchantments = ItemLimits.getMaxEnchantments(result);
                if (result.getEnchantments().size() > maxEnchantments) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                }
            }
        }

        if (event.getInventory() instanceof )
    }
}
