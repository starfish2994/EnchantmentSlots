package cn.superiormc.enchantmentslots.objects.actions;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ActionAnnouncement extends AbstractRunAction {

    public ActionAnnouncement() {
        super("announcement");
        setRequiredArgs("message");
    }

    @Override
    protected void onDoAction(ObjectSingleAction singleAction, Player player, int amount) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player p : players) {
            EnchantmentSlots.methodUtil.sendMessage(p, singleAction.getString("message", player, amount));
        }
    }
}
