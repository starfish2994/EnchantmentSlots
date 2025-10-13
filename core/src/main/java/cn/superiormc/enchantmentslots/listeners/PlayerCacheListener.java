package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.protolcol.ProtocolLib.SetCursorItem;
import cn.superiormc.enchantmentslots.utils.SchedulerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collection;

public class PlayerCacheListener implements Listener {

    public static Collection<Player> loadedPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        long time = ConfigManager.configManager.getLong("settings.set-slot-trigger.SetSlotPacket.remove-illegal-excess-enchant.ignore-join-time", -1);
        if (time < 0) {
            loadedPlayers.add(event.getPlayer());
            return;
        }
        SchedulerUtil.runTaskLater(() -> loadedPlayers.add(event.getPlayer()), time);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        loadedPlayers.remove(event.getPlayer());
        SetCursorItem.hashedStackMap.remove(event.getPlayer());
    }
}
