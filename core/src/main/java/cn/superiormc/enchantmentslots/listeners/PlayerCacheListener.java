package cn.superiormc.enchantmentslots.listeners;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.ListenerManager;
import cn.superiormc.enchantmentslots.utils.SchedulerUtil;
import com.github.retrooper.packetevents.protocol.item.HashedStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerCacheListener implements Listener {

    public static Map<Player, HashedStack> hashedStackMap = new HashMap<>();

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
        if (ListenerManager.listenerManager.getPlugin() != null && ListenerManager.listenerManager.getPlugin().equals("packetevents")) {
            hashedStackMap.remove(event.getPlayer());
        }
    }
}
