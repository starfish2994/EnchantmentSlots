package cn.superiormc.enchantmentslots.hooks.mythicchanger;

import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.mythicchanger.objects.changes.AbstractChangesRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ResetSlot extends AbstractChangesRule {

    public ResetSlot() {
        super();
    }

    @Override
    public ItemStack setChange(ConfigurationSection section,
                               ItemStack original,
                               ItemStack item,
                               Player player,
                               boolean fakeOrReal,
                               boolean isPlayerInventory) {
        String itemID = CheckValidHook.checkValid(item);
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
        return ItemModify.resetSlot(item, defaultSlot, itemID);
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return section.getBoolean("es-reset-slot", false);
    }
}
