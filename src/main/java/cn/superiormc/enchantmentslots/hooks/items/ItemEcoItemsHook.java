package cn.superiormc.enchantmentslots.hooks.items;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import com.willfp.ecoarmor.sets.ArmorSet;
import com.willfp.ecoarmor.sets.ArmorUtils;
import com.willfp.ecoarmor.upgrades.Tier;
import com.willfp.ecoitems.items.EcoItem;
import com.willfp.ecoitems.items.EcoItems;
import com.willfp.ecoitems.items.ItemUtilsKt;
import com.willfp.ecoitems.rarity.Rarity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemEcoItemsHook extends AbstractItemHook {

    public ItemEcoItemsHook() {
        super("EcoItems");
    }

    @Override
    public ItemStack getHookItemByID(Player player, String hookItemID) {
        EcoItem ecoItems = EcoItems.INSTANCE.getByID(hookItemID);
        if (ecoItems == null) {
            return returnNullItem(hookItemID);
        }
        return ecoItems.getItemStack();
    }

    @Override
    public String getIDByItemStack(ItemStack hookItem) {
        EcoItem tempVal1 = ItemUtilsKt.getEcoItem(hookItem);
        if (tempVal1 == null) {
            return null;
        }
        return tempVal1.getID();
    }

    @Override
    public String getSimplyIDByItemStack(ItemStack hookItem) {
        EcoItem tempVal1 = ItemUtilsKt.getEcoItem(hookItem);
        if (tempVal1 == null) {
            return null;
        }
        if (ConfigManager.configManager.getBoolean("settings.use-tier-identify-slots", false)) {
            Rarity rarity = tempVal1.getRarity();
            if (rarity == null) {
                return tempVal1.getID();
            }
            return rarity.getID();
        }
        return tempVal1.getID();
    }
}
