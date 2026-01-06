package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.entity.Player;

public class ActionMessage extends AbstractRunAction {

    public ActionMessage() {
        super("message");
        setRequiredArgs("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        EnchantmentSlots.methodUtil.sendChat(player, singleAction.getString("message", player, amount));
    }
}
