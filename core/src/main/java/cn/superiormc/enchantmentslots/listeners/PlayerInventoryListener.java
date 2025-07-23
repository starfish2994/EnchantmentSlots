package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import cn.superiormc.enchantmentslots.objects.ObjectExtraSlotsItem;
import cn.superiormc.enchantmentslots.methods.SlotUtil;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
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
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        ItemStack targetItem = event.getCurrentItem();
        if (!ItemUtil.isValid(targetItem)) {
            return;
        }
        ItemStack extraItem = event.getCursor();
        if (extraItem.getType().isAir()) {
            return;
        }
        ObjectExtraSlotsItem item = ConfigManager.configManager.getExtraSlotItemValue(extraItem);
        if (item == null || !item.canApply(player, targetItem)) {
            return;
        }
        int value = item.getAddSlot();
        int baseValue = SlotUtil.getSlot(targetItem);
        if (baseValue == 0) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            LanguageManager.languageManager.sendStringText(player, "error-creative-mode");
            return;
        }
        int maxValue = ConfigManager.configManager.getMaxLimits(targetItem, player);
        if (baseValue >= maxValue) {
            LanguageManager.languageManager.sendStringText(player, "max-slots-reached");
            return;
        }
        if (maxValue != -1 && baseValue + value > maxValue) {
            if (ConfigManager.configManager.getBoolean("settings.cancel-add-slot-if-reached-max-slot", true)) {
                LanguageManager.languageManager.sendStringText(player, "max-slots-reached");
            } else {
                extraItem.setAmount(extraItem.getAmount() - 1);
                if (value == 0) {
                    item.doFailAction(player);
                } else {
                    SlotUtil.setSlot(targetItem, maxValue, true);
                    item.doSuccessAction(player);
                }
            }
            return;
        }
        extraItem.setAmount(extraItem.getAmount() - 1);
        if (value == 0) {
            item.doFailAction(player);
        } else {
            SlotUtil.setSlot(targetItem, baseValue + value, true);
            item.doSuccessAction(player);
        }
    }
}
