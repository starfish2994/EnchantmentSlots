package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof org.bukkit.inventory.PlayerInventory)) {
            return;
        }
        if (!event.getClick().isRightClick() && !event.getClick().isLeftClick()) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        ItemStack targetItem = event.getCurrentItem();
        if (targetItem == null ||
        targetItem.getType().isAir()) {
            return;
        }
        ItemStack extraItem = event.getCursor();
        if (extraItem == null ||
        extraItem.getType().isAir()) {
            return;
        }
        ExtraSlotsItem item = ExtraSlotsItem.getExtraSlotItemValue(extraItem);
        if (item == null || !item.canApply(targetItem)) {
            return;
        }
        int value = item.getAddSlot();
        if (value == 0) {
            item.doFailAction(player);
            return;
        }
        int baseValue = ItemLimits.getMaxEnchantments(targetItem, ConfigReader.getDefaultLimits(player, targetItem));
        if (baseValue == 0) {
            return;
        }
        int maxValue = ConfigReader.getMaxLimits(player, targetItem);
        if (maxValue != -1 && baseValue + value > maxValue) {
            if (ConfigReader.getCancelMaxLimits() || baseValue >= maxValue) {
                item.doFailAction(player);
                player.sendMessage(Messages.getMessages("max-slots-reached"));
            } else {
                extraItem.setAmount(extraItem.getAmount() - 1);
                ItemLimits.setMaxEnchantments(targetItem, maxValue);
                item.doSuccessAction(player);
            }
            return;
        }
        extraItem.setAmount(extraItem.getAmount() - 1);
        ItemLimits.setMaxEnchantments(targetItem, baseValue + value);
        item.doSuccessAction(player);
    }
}
