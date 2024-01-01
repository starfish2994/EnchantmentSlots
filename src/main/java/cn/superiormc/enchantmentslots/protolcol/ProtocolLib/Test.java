package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;

import java.util.Set;

public class Test extends GeneralPackets {

    // 客户端发给服务端
    public Test() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        Set<PacketType> tempVal = PacketType.Play.Server.getInstance().values();
        tempVal.remove(PacketType.Play.Server.ENTITY_TELEPORT);
        tempVal.remove(PacketType.Play.Server.ENTITY_VELOCITY);
        tempVal.remove(PacketType.Play.Server.REL_ENTITY_MOVE);
        tempVal.remove(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        tempVal.remove(PacketType.Play.Server.MAP_CHUNK);
        tempVal.remove(PacketType.Play.Server.UPDATE_TIME);
        tempVal.remove(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        tempVal.remove(PacketType.Play.Server.ENTITY_LOOK);
        tempVal.remove(PacketType.Play.Server.BUNDLE);
        tempVal.remove(PacketType.Play.Server.SPAWN_ENTITY);
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ListenerPriority.NORMAL, tempVal) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found " + event.getPacketType().name() + " packet.");
            }
        };
    }
}
