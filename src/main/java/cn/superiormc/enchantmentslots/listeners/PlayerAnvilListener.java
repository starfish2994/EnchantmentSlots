package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerAnvilListener implements Listener {

    @EventHandler
    public void onAnvilItem(InventoryClickEvent event) {
        if (event.getInventory() instanceof AnvilInventory) {
            if (event.getSlot() != 2) {
                return;
            }
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player)event.getWhoClicked();
            AnvilInventory inventory = (AnvilInventory) event.getInventory();
            ItemStack item = inventory.getItem(0);
            if (item == null) {
                return;
            }
            String itemID = CheckValidHook.checkValid(item);
            int defaultSlot = ConfigReader.getDefaultLimits(player, itemID);
            ItemStack result = inventory.getItem(2);
            if (result != null) {
                if (ConfigReader.getAnvilItemTrigger()) {
                    ItemModify.addLore(result, defaultSlot, itemID);
                }
                int maxEnchantments = ItemLimits.getMaxEnchantments(result, defaultSlot, itemID);
                if (ItemUtil.getEnchantments(result, false).size() > maxEnchantments) {
                    inventory.setRepairCost(0);
                    event.setCancelled(true);
                    if (ConfigReader.getCloseInventory()) {
                        player.closeInventory();
                    }
                    player.giveExp(-1);
                    if (player.getTotalExperience() > 0) {
                        player.giveExp(1);
                    }
                    if (ConfigReader.getAnvilItemUpdate()) {
                        item.setAmount(item.getAmount());
                    }
                    player.sendMessage(Messages.getMessages("slots-limit-reached"));
                }
            }
        }
    }
}
