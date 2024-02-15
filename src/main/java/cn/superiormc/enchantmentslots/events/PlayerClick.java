package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class PlayerClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
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
            int defaultSlot = ConfigReader.getDefaultLimits(player, item);
            ItemModify.addLore(item, defaultSlot);
            ItemStack result = inventory.getItem(2);
            if (result != null) {
                int maxEnchantments = ItemLimits.getMaxEnchantments(result, defaultSlot);
                if (result.getEnchantments().size() > maxEnchantments) {
                    event.setCancelled(true);
                    if (ConfigReader.getCloseInventory()) {
                        player.closeInventory();
                    }
                    player.sendMessage(Messages.getMessages("slots-limit-reached"));
                }
            }
        }
    }
}
