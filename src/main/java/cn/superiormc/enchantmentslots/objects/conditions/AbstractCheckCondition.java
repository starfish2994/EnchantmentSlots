package cn.superiormc.enchantmentslots.objects.conditions;

import cn.superiormc.enchantmentslots.managers.ErrorManager;
import org.bukkit.entity.Player;

public abstract class AbstractCheckCondition {

    private final String type;

    private String[] requiredArgs;

    public AbstractCheckCondition(String type) {
        this.type = type;
    }

    protected void setRequiredArgs(String... requiredArgs) {
        this.requiredArgs = requiredArgs;
    }

    public boolean checkCondition(ObjectSingleCondition singleCondition, Player player, int amount) {
        if (requiredArgs != null) {
            for (String arg : requiredArgs) {
                if (!singleCondition.getSection().contains(arg)) {
                    ErrorManager.errorManager.sendErrorMessage("§cError: Your condition missing required arg: " + arg + ".");
                    return true;
                }
            }
        }
        return onCheckCondition(singleCondition, player, amount);
    }

    protected abstract boolean onCheckCondition(ObjectSingleCondition singleCondition, Player player, int amount);

    public String getType() {
        return type;
    }
}
