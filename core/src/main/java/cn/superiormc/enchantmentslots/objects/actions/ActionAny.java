package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.objects.ObjectAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionAny extends AbstractRunAction {

    public ActionAny() {
        super("any");
        setRequiredArgs("actions");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        ConfigurationSection chanceSection = singleAction.getSection().getConfigurationSection("actions");
        if (chanceSection == null) {
            return;
        }
        ObjectAction action = new ObjectAction(chanceSection);
        action.runRandomEveryActions(player, amount, singleAction.getInt("amount", 1));
    }
}
