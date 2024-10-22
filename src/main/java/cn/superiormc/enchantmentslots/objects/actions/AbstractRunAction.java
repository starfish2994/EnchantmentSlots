package cn.superiormc.enchantmentslots.objects.actions;


import cn.superiormc.enchantmentslots.managers.ErrorManager;
import org.bukkit.entity.Player;

public abstract class AbstractRunAction {

    private final String type;

    private String[] requiredArgs;

    public AbstractRunAction(String type) {
        this.type = type;
    }

    protected void setRequiredArgs(String... requiredArgs) {
        this.requiredArgs = requiredArgs;
    }

    public void runAction(ObjectSingleAction singleAction, Player player, int amount) {
        for (String arg : requiredArgs) {
            if (!singleAction.getSection().contains(arg)) {
                ErrorManager.errorManager.sendErrorMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cError: Your action missing required arg: " + arg + ".");
                return;
            }
        }
        onDoAction(singleAction, player, amount);
    }

    protected abstract void onDoAction(ObjectSingleAction singleAction, Player player, int amount);

    public String getType() {
        return type;
    }
}
