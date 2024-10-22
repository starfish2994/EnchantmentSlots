package cn.superiormc.enchantmentslots.objects.conditions;

import cn.superiormc.enchantmentslots.objects.ObjectCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ConditionAny extends AbstractCheckCondition {

    public ConditionAny() {
        super("any");
        setRequiredArgs("conditions");
    }

    @Override
    protected boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, int amount) {
        ConfigurationSection anySection = singleCondition.getSection().getConfigurationSection("conditions");
        if (anySection == null) {
            return true;
        }
        ObjectCondition condition = new ObjectCondition(anySection);
        return condition.getAnyBoolean(player, amount);
    }
}
