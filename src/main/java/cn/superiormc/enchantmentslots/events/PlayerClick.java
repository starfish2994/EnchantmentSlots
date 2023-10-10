package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerClick implements Listener {
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

        if (event.getInventory() instanceof EnchantingInventory) {
            ItemStack item = event.getCursor();
            EnchantingInventory inventory = (EnchantingInventory) event.getInventory();
            ItemStack lapis = inventory.getItem(1);
            if (item != null && lapis != null && lapis.getType() == Material.LAPIS_LAZULI) {
                int maxEnchantments = ItemLimits.getMaxEnchantments(item);
                // 检查物品是否已经有超过指定数量的附魔
                if (item.getEnchantments().size() >= maxEnchantments) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                }
            }
        }
    }
}
