package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSmithListener implements Listener {
    @EventHandler
    public void onSmithItem(SmithItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getInventory().getResult();
        if (item == null || item.getType().isAir()) {
            return;
        }
        String itemID = CheckValidHook.checkValid(item);
        int defaultSlot = ConfigReader.getDefaultLimits(player, itemID);
        if (ConfigReader.getSmithItemTrigger()) {
            if (ConfigReader.getSmithItemReset()) {
                event.getInventory().setResult(ItemModify.removeAndAddLore(item, defaultSlot, itemID));
            } else {
                event.getInventory().setResult(ItemModify.addLore(item, defaultSlot, itemID));
            }
        }
        int maxEnchantments = ItemLimits.getMaxEnchantments(item, defaultSlot, itemID);
        if (ItemUtil.getEnchantments(item, false).size() > maxEnchantments) {
            event.setCancelled(true);
            if (ConfigReader.getCloseInventory()) {
                player.closeInventory();
            }
            player.sendMessage(Messages.getMessages("slots-limit-reached-after-smith"));
        }
    }
}
