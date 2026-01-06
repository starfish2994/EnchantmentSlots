package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.methods.SlotUtil;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.Bukkit;
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
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §f" +
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
            if (!ItemUtil.isValid(tempItemStack)) {
                tempItemStack = player.getItemOnCursor();
                if (tempItemStack.getType().isAir()) {
                    return;
                }
            }
            int defaultSlot = ConfigManager.configManager.getDefaultLimits(tempItemStack, player);
            SlotUtil.setSlot(tempItemStack, defaultSlot, false);
        }
    }
}
