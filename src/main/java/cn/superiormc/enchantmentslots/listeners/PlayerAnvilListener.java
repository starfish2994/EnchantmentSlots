package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import cn.superiormc.enchantmentslots.methods.SlotUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerAnvilListener implements Listener {

    @EventHandler
    public void onAnvilItem(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory inventory) {
            if (event.getSlot() != 2) {
                return;
            }
            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }
            ItemStack item = inventory.getItem(0);
            if (item == null) {
                return;
            }
            ItemStack result = inventory.getItem(2);
            if (result != null) {
                if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.AnvilItemEvent.enabled", true)) {
                    SlotUtil.setSlot(result, player, false);
                }
                int maxEnchantments = SlotUtil.getSlot(item);
                if (!ConfigManager.configManager.isIgnore(item) && ItemUtil.getEnchantments(result, false).size() > maxEnchantments) {
                    inventory.setRepairCost(0);
                    event.setCancelled(true);
                    if (ConfigManager.configManager.getBoolean("settings.close-inventory-if-reached-limit", true)) {
                        player.closeInventory();
                    }
                    player.giveExp(-1);
                    if (player.getTotalExperience() > 0) {
                        player.giveExp(1);
                    }
                    if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.AnvilItemEvent.update-item", false)) {
                        item.setAmount(item.getAmount());
                    }
                    LanguageManager.languageManager.sendStringText(player, "slots-limit-reached");
                }
            }
        }
    }
}
