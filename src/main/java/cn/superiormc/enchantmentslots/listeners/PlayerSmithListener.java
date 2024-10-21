package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSmithListener implements Listener {
    @EventHandler
    public void onSmithItem(SmithItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack item = event.getInventory().getResult();
        if (item == null || item.getType().isAir()) {
            return;
        }
        String itemID = HookManager.hookManager.parseItemID(item);
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
        int maxEnchantments = ItemLimits.getMaxEnchantments(item, defaultSlot, itemID);
        if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.SmithItemEvent.enabled", true)) {
            if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.SmithItemEvent.keep-greater-slot", true) && maxEnchantments > defaultSlot) {
                defaultSlot = maxEnchantments;
            }
            if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.SmithItemEvent.reset-previous-slot", true)) {
                event.getInventory().setResult(ItemModify.resetSlot(item, defaultSlot, itemID));
            } else {
                event.getInventory().setResult(ItemModify.setSlot(item, defaultSlot, itemID));
            }
            if (defaultSlot != maxEnchantments) {
                maxEnchantments = defaultSlot;
            }
        }
        if (ItemUtil.getEnchantments(item, false).size() > maxEnchantments) {
            event.setCancelled(true);
            if (ConfigManager.configManager.getBoolean("settings.close-inventory-if-reached-limit", true)) {
                player.closeInventory();
            }
            LanguageManager.languageManager.sendStringText(player, "slots-limit-reached-after-smith");
        }
    }
}
