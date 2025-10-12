package cn.superiormc.enchantmentslots.hooks.items;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemMMOItemsHook extends AbstractItemHook {

    public ItemMMOItemsHook() {
        super("MMOItems");
    }

    @Override
    public ItemStack getHookItemByID(Player player, String hookItemID) {
        if (hookItemID.split(";;").length != 2) {
            return returnNullItem(hookItemID);
        }
        ItemStack resultItem = MMOItems.plugin.getItem(hookItemID.split(";;")[0], hookItemID.split(";;")[1]);
        if (resultItem == null) {
            return returnNullItem(hookItemID);
        }
        return resultItem;
    }

    @Override
    public String getIDByItemStack(ItemStack hookItem) {
        String tempVal1 = MMOItems.getID(hookItem);
        if (tempVal1 == null || tempVal1.isEmpty()) {
            return null;
        }
        String tempVal2 = MMOItems.getTypeName(hookItem);
        if (tempVal2 == null || tempVal2.isEmpty()) {
            return null;
        }
        else {
            return tempVal2 + ";;" + tempVal1;
        }
    }

    @Override
    public String getSimplyIDByItemStack(ItemStack hookItem) {
        String tempVal1 = MMOItems.getID(hookItem);
        if (tempVal1 != null && !tempVal1.isEmpty()) {
            if (ConfigManager.configManager.getBoolean("settings.use-tier-identify-slots", false)) {
                ItemTier tempVal2 = ItemTier.ofItem(NBTItem.get(hookItem));
                if (tempVal2 != null) {
                    return tempVal2.getId();
                }
            }
            return tempVal1;
        }
        return null;
    }
}
