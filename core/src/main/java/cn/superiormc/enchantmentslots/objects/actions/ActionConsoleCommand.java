package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import org.bukkit.entity.Player;

public class ActionConsoleCommand extends AbstractRunAction {

    public ActionConsoleCommand() {
        super("console_command");
        setRequiredArgs("command");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        EnchantmentSlots.methodUtil.dispatchCommand(singleAction.getString("command", player, amount));
    }
}
