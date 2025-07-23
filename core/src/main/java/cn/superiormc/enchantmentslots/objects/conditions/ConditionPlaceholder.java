package cn.superiormc.enchantmentslots.objects.conditions;

import cn.superiormc.enchantmentslots.managers.ErrorManager;
import org.bukkit.entity.Player;

public class ConditionPlaceholder extends AbstractCheckCondition {

    public ConditionPlaceholder() {
        super("placeholder");
        setRequiredArgs("placeholder", "rule", "value");
    }

    @Override
    protected boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, int amount) {
        String placeholder = singleCondition.getString("placeholder", player, amount);
        String value = singleCondition.getString("value", player, amount);
        switch (singleCondition.getString("rule")) {
            case ">=":
                return Double.parseDouble(placeholder) >= Double.parseDouble(value);
            case ">":
                return Double.parseDouble(placeholder) > Double.parseDouble(value);
            case "=":
                return Double.parseDouble(placeholder) == Double.parseDouble(value);
            case "<":
                return Double.parseDouble(placeholder) < Double.parseDouble(value);
            case "<=":
                return Double.parseDouble(placeholder) <= Double.parseDouble(value);
            case "==":
                return placeholder.equals(value);
            case "!=":
                return !placeholder.equals(value);
            case "*=":
                return placeholder.contains(value);
            case "=*":
                return value.contains(placeholder);
            case "!*=":
                return !placeholder.contains(value);
            case "!=*":
                return !value.contains(placeholder);
            default:
                ErrorManager.errorManager.sendErrorMessage("§cError: Your placeholder condition can not being correctly load.");
                return true;
        }
    }
}
