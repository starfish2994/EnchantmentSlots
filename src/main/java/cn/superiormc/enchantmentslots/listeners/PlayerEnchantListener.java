package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
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
        int defaultSlot = ConfigReader.getDefaultLimits(player, itemID);
        if (ConfigReader.getEnchantItemTrigger()) {
            ItemModify.addLore(item, defaultSlot, itemID);
        }
        int maxEnchantments = ItemLimits.getMaxEnchantments(item, defaultSlot, itemID);
        if (event.getEnchantsToAdd().size() + ItemUtil.getEnchantments(item, false).size() > maxEnchantments) {
            Bukkit.getScheduler().runTask(EnchantmentSlots.instance, () -> {
                int removeAmount = item.getEnchantments().size() - maxEnchantments;
                if (!ConfigReader.getEnchantCancel() && item.getType() != Material.BOOK && removeAmount < event.getEnchantsToAdd().size()) {
                    for (Enchantment enchant : item.getEnchantments().keySet()) {
                        if (removeAmount <= 0) {
                            break;
                        }
                        ItemMeta meta = item.getItemMeta();
                        if (meta == null) {
                            break;
                        }
                        meta.removeEnchant(enchant);
                        item.setItemMeta(meta);
                        removeAmount--;
                    }
                    player.sendMessage(Messages.getMessages("slots-limit-reached-enchant"));
                } else {
                    event.setCancelled(true);
                    if (ConfigReader.getCloseInventory()) {
                        player.closeInventory();
                    }
                    player.sendMessage(Messages.getMessages("slots-limit-reached"));
                }
            });
        }

    }
}
