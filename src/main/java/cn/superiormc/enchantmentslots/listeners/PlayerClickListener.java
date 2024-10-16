package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerClickListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (ConfigManager.configManager.getBoolean("debug", false)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                    "Found WindowsClick packet.");
        }
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().equals(player.getOpenInventory().getBottomInventory())) {
            ItemStack tempItemStack = event.getCurrentItem();
            if (tempItemStack == null || tempItemStack.getType().isAir()) {
                tempItemStack = player.getItemOnCursor();
                if (tempItemStack.getType().isAir()) {
                    return;
                }
            }
            boolean isBook = ConfigManager.configManager.getBoolean("settings.set-slot-trigger.black-book",
                    true) && (tempItemStack.getType().equals(Material.BOOK) || tempItemStack.getType().equals(Material.ENCHANTED_BOOK));
            if (!isBook) {
                String itemID = CheckValidHook.checkValid(tempItemStack);
                int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
                ItemModify.setSlot(tempItemStack, defaultSlot, itemID);
            }
        }
    }
}
