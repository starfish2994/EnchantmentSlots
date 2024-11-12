package cn.superiormc.enchantmentslots.hooks.mythicchanger;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.mythicchanger.objects.ObjectSingleChange;
import cn.superiormc.mythicchanger.objects.changes.AbstractChangesRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class SetSlot extends AbstractChangesRule {

    public SetSlot() {
        super();
    }

    @Override
    public ItemStack setChange(ObjectSingleChange singleChange) {
        String amount = singleChange.getString("es-set-slot", "true");
        if (amount.equals("true")) {
            String itemID = HookManager.hookManager.parseItemID(singleChange.getItem());
            int defaultSlot = ConfigManager.configManager.getDefaultLimits(singleChange.getPlayer(), itemID);
            return ItemModify.setSlot(singleChange.getItem(), defaultSlot, itemID);
        } else {
            ItemLimits.setMaxEnchantments(singleChange.getItem(), Integer.parseInt(amount));
            return singleChange.getItem();
        }
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return !section.contains("es-set-slot");
    }
}
