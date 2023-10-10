package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEnchant implements Listener {
    @EventHandler
    public void onEnchantItem(PrepareItemEnchantEvent event) {
        if (event.getOffers().length == 0) {
            return;
        }
        ItemStack item = event.getItem();
        Player player = event.getEnchanter();
        int maxEnchantments = ItemLimits.getMaxEnchantments(item);
        if (item.getEnchantments().size() > maxEnchantments) {
            event.setCancelled(true);
        }
    }
}
