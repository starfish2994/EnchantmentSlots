package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
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
        String itemID = CheckValidHook.checkValid(item);
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
        if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.EnchantItemEvent.enabled", true)) {
            ItemModify.setSlot(item, defaultSlot, itemID);
        }
        int maxEnchantments = ItemLimits.getMaxEnchantments(item, defaultSlot, itemID);
        if (event.getEnchantsToAdd().size() + ItemUtil.getEnchantments(item, false).size() > maxEnchantments) {
            int removeAmount = item.getEnchantments().size() - maxEnchantments;
                if (!ConfigManager.configManager.getBoolean("settings.set-slot-trigger.EnchantItemEvent.cancel-if-reached-slot", true) && item.getType() != Material.BOOK && removeAmount < event.getEnchantsToAdd().size()) {
                    Bukkit.getScheduler().runTask(EnchantmentSlots.instance, () -> {
                        int realRemoveAmount = removeAmount;
                        for (Enchantment enchant : item.getEnchantments().keySet()) {
                            if (realRemoveAmount <= 0) {
                                break;
                            }
                            ItemMeta meta = item.getItemMeta();
                            if (meta == null) {
                                break;
                            }
                            meta.removeEnchant(enchant);
                            item.setItemMeta(meta);
                            realRemoveAmount--;
                        }
                        LanguageManager.languageManager.sendStringText(player, "slots-limit-reached-enchant");
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
