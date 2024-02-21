package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerClickListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (ConfigReader.getDebug()) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                    "Found WindowsClick packet.");
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory().equals(player.getOpenInventory().getBottomInventory())) {
            ItemStack tempItemStack = event.getCurrentItem();
            if (tempItemStack == null || tempItemStack.getType().isAir()) {
                tempItemStack = player.getItemOnCursor();
                if (tempItemStack.getType().isAir()) {
                    return;
                }
            }
            ItemModify.addLore(player, tempItemStack);
        }
    }
}
