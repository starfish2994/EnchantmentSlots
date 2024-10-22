package cn.superiormc.enchantmentslots.objects.conditions;

import cn.superiormc.enchantmentslots.managers.ConditionManager;
import cn.superiormc.enchantmentslots.objects.AbstractSingleRun;
import cn.superiormc.enchantmentslots.objects.ObjectCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ObjectSingleCondition extends AbstractSingleRun {

    private final ObjectCondition condition;

    public ObjectSingleCondition(ObjectCondition condition, ConfigurationSection conditionSection) {
        super(conditionSection);
        this.condition = condition;
    }

    public boolean checkBoolean(Player player, int amount) {
        return ConditionManager.conditionManager.checkBoolean(this, player, amount);
    }

    public ObjectCondition getCondition() {
        return condition;
    }

}
