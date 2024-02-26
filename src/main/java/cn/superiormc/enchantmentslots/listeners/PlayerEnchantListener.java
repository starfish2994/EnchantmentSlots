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
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEnchantListener implements Listener {
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        int defaultSlot = ConfigReader.getDefaultLimits(player, CheckValidHook.checkValid(item));
        if (ConfigReader.getEnchantItemTrigger()) {
            ItemModify.addLore(item, defaultSlot);
        }
        int maxEnchantments = ItemLimits.getMaxEnchantments(item, defaultSlot);
        if (event.getEnchantsToAdd().size() + ItemUtil.getEnchantments(item).size() > maxEnchantments) {
            event.setCancelled(true);
            if (ConfigReader.getCloseInventory()) {
                player.closeInventory();
            }
            player.sendMessage(Messages.getMessages("slots-limit-reached"));
        }
    }
}
