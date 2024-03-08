package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof PlayerInventory)) {
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
        if (targetItem == null || targetItem.getType().isAir()) {
            return;
        }
        ItemStack extraItem = event.getCursor();
        if (extraItem == null || extraItem.getType().isAir()) {
            return;
        }
        ExtraSlotsItem item = ExtraSlotsItem.getExtraSlotItemValue(extraItem);
        if (item == null || !item.canApply(targetItem)) {
            return;
        }
        int value = item.getAddSlot();
        String itemID = CheckValidHook.checkValid(targetItem);
        int baseValue = ItemLimits.getMaxEnchantments(targetItem, ConfigReader.getDefaultLimits(player, itemID), itemID);
        if (baseValue == 0) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(Messages.getMessages("error-creative-mode"));
            return;
        }
        int maxValue = ConfigReader.getMaxLimits(player, targetItem);
        if (baseValue >= maxValue) {
            player.sendMessage(Messages.getMessages("max-slots-reached"));
            return;
        }
        if (maxValue != -1 && baseValue + value > maxValue) {
            if (ConfigReader.getCancelMaxLimits()) {
                player.sendMessage(Messages.getMessages("max-slots-reached"));
            } else {
                extraItem.setAmount(extraItem.getAmount() - 1);
                if (value == 0) {
                    item.doFailAction(player);
                } else {
                    ItemLimits.setMaxEnchantments(targetItem, maxValue);
                    item.doSuccessAction(player);
                }
            }
            return;
        }
        extraItem.setAmount(extraItem.getAmount() - 1);
        if (value == 0) {
            item.doFailAction(player);
        } else {
            ItemLimits.setMaxEnchantments(targetItem, baseValue + value);
            item.doSuccessAction(player);
        }
    }
}
