package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.methods.AddLore;
import com.ghostchu.quickshop.api.event.display.ItemPreviewComponentPrePopulateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class QuickShopListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemPreviewPreparing(final ItemPreviewComponentPrePopulateEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        final ItemStack item = event.getItemStack().clone();
        event.setItemStack(AddLore.addLore(item, event.getPlayer()));
    }
}
