package cn.superiormc.enchantmentslots.hooks.mythicchanger;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.mythicchanger.objects.ObjectSingleChange;
import cn.superiormc.mythicchanger.objects.changes.AbstractChangesRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ResetSlot extends AbstractChangesRule {

    public ResetSlot() {
        super();
    }

    @Override
    public ItemStack setChange(ObjectSingleChange singleChange) {
        String itemID = HookManager.hookManager.parseItemID(singleChange.getItem());
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(singleChange.getPlayer(), itemID);
        return ItemModify.resetSlot(singleChange.getItem(), defaultSlot, itemID);
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return section.getBoolean("es-reset-slot", false);
    }
}
