package cn.superiormc.enchantmentslots.hooks.mythicchanger;

import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.mythicchanger.objects.ObjectSingleChange;
import cn.superiormc.mythicchanger.objects.changes.AbstractChangesRule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class AddESLore extends AbstractChangesRule {

    public AddESLore() {
        super();
    }

    @Override
    public ItemStack setChange(ObjectSingleChange singleChange) {
        return ItemModify.clientToServer(singleChange.getItem());
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return section.getBoolean("add-es-slot", false);
    }
}
