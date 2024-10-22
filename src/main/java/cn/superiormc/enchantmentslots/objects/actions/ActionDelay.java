package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.objects.ObjectAction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionDelay extends AbstractRunAction {

    public ActionDelay() {
        super("delay");
        setRequiredArgs("time", "actions");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        ConfigurationSection chanceSection = singleAction.getSection().getConfigurationSection("actions");
        if (chanceSection == null) {
            return;
        }
        long time = singleAction.getSection().getLong("time");
        ObjectAction action = new ObjectAction(chanceSection);
        Bukkit.getScheduler().runTaskLater(EnchantmentSlots.instance, () -> action.runAllActions(player, amount), time);
    }
}
