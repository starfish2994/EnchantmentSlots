package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.entity.Player;

public class ActionActionBar extends AbstractRunAction {

    public ActionActionBar() {
        super("action_bar");
        setRequiredArgs("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        String msg = singleAction.getString("message", player, amount);
        EnchantmentSlots.methodUtil.sendActionBar(player, msg);
    }
}
