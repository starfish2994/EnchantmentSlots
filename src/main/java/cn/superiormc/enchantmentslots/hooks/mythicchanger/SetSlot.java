package cn.superiormc.enchantmentslots.hooks.mythicchanger;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.mythicchanger.objects.changes.AbstractChangesRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetSlot extends AbstractChangesRule {

    public SetSlot() {
        super();
    }

    @Override
    public ItemStack setChange(ConfigurationSection section,
                               ItemStack original,
                               ItemStack item,
                               Player player,
                               boolean fakeOrReal,
                               boolean isPlayerInventory) {
        String amount = section.getString("es-set-slot", "true");
        if (amount.equals("true")) {
            String itemID = HookManager.hookManager.parseItemID(item);
            int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
            return ItemModify.setSlot(item, defaultSlot, itemID);
        } else {
            ItemLimits.setMaxEnchantments(item, Integer.parseInt(amount));
            return item;
        }
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return !section.contains("es-set-slot");
    }
}
