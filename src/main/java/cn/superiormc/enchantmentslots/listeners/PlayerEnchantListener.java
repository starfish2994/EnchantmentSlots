package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.methods.EnchantsUtil;
import cn.superiormc.enchantmentslots.utils.SchedulerUtil;
import cn.superiormc.enchantmentslots.methods.SlotUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerEnchantListener implements Listener {
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.EnchantItemEvent.enabled", true)) {
            SlotUtil.setSlot(item, player, false);
        }
        int maxEnchantments = SlotUtil.getSlot(item);
        if (!ConfigManager.configManager.isIgnore(item) && EnchantsUtil.getUsedSlot(event.getEnchantsToAdd().keySet()) + EnchantsUtil.getUsedSlot(item) > maxEnchantments) {
            int removeAmount = EnchantsUtil.getUsedSlot(event.getEnchantsToAdd().keySet()) + EnchantsUtil.getUsedSlot(item) - maxEnchantments;
            if (!ConfigManager.configManager.getBoolean("settings.set-slot-trigger.EnchantItemEvent.cancel-if-reached-slot", true) &&
                    item.getType() != Material.BOOK && removeAmount > 0) {
                SchedulerUtil.runSync(() -> {
                    ItemMeta otherMeta = item.getItemMeta();
                    int realRemoveAmount = removeAmount;
                    for (Enchantment enchant : otherMeta.getEnchants().keySet()) {
                        if (realRemoveAmount <= 0) {
                            break;
                        }
                        otherMeta.removeEnchant(enchant);
                        realRemoveAmount = realRemoveAmount - EnchantsUtil.getUsedSlot(enchant);
                    }
                    item.setItemMeta(otherMeta);
                    LanguageManager.languageManager.sendStringText(player, "slots-limit-reached-enchant", "max", String.valueOf(maxEnchantments), "remove", String.valueOf(removeAmount));
                });
            } else {
                event.setCancelled(true);
                if (ConfigManager.configManager.getBoolean("settings.close-inventory-if-reached-limit", true)) {
                    player.closeInventory();
                }
                LanguageManager.languageManager.sendStringText(player, "slots-limit-reached");
            }
        }

    }
}
