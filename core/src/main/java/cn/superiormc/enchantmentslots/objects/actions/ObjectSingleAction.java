package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.managers.ActionManager;
import cn.superiormc.enchantmentslots.objects.AbstractSingleRun;
import cn.superiormc.enchantmentslots.objects.ObjectAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ObjectSingleAction extends AbstractSingleRun {

    private final ObjectAction action;


    public ObjectSingleAction(ObjectAction action, ConfigurationSection actionSection) {
        super(actionSection);
        this.action = action;
    }

    public void doAction(Player player, int amount) {
        ActionManager.actionManager.doAction(this, player, amount);
    }


    public ObjectAction getAction() {
        return action;
    }

}
