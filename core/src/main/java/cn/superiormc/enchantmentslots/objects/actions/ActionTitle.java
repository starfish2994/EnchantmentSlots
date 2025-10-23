package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.entity.Player;

public class ActionTitle extends AbstractRunAction {

    public ActionTitle() {
        super("title");
        setRequiredArgs("main-title", "sub-title", "fade-in", "stay", "fade-out");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        EnchantmentSlots.methodUtil.sendTitle(player,
                singleAction.getString("main-title", player, amount),
                singleAction.getString("sub-title", player, amount),
                singleAction.getInt("fade-in"),
                singleAction.getInt("stay"),
                singleAction.getInt("fade-out"));
    }
}
