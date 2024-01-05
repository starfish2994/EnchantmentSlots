package cn.superiormc.enchantmentslots.events;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventory implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryClickEvent event) {
        if (event.getInventory() instanceof org.bukkit.inventory.PlayerInventory) {
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
        int value = ExtraSlotsItem.getExtraSlotItemValue(extraItem);
        if (value == 0) {
            return;
        }
        int baseValue = ItemLimits.getMaxEnchantments(player, targetItem);
        if (baseValue == 0) {
            return;
        }
        int maxValue = ConfigReader.getMaxLimits(player, targetItem);
        if (maxValue != -1 && baseValue + value >= maxValue) {
            if (ConfigReader.getCancelMaxLimits() || baseValue >= maxValue) {
                player.sendMessage(ConfigReader.getMessages("max-slots-reached"));
                return;
            } else {
                extraItem.setAmount(extraItem.getAmount() - 1);
                ItemLimits.setMaxEnchantments(targetItem, maxValue);
                player.sendMessage(ConfigReader.getMessages("success-add").replace("%amount%", String.valueOf(maxValue - baseValue)));
                return;
            }
        }
        extraItem.setAmount(extraItem.getAmount() - 1);
        ItemLimits.setMaxEnchantments(targetItem, baseValue + value);
        player.sendMessage(ConfigReader.getMessages("success-add").replace("%amount%", String.valueOf(value)));
    }
}
