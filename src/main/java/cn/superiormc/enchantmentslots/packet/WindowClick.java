package cn.superiormc.enchantmentslots.packet;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ItemModify;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.inventory.ItemStack;

public class WindowClick extends GeneralPackets{

    // 客户端发给服务端
    public WindowClick() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ListenerPriority.NORMAL, PacketType.Play.Client.WINDOW_CLICK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                for (int i = 0; i < itemStackStructureModifier.size(); i++) {
                    ItemStack clientItemStack = itemStackStructureModifier.read(i);
                    if (clientItemStack.getType().isAir()) {
                        continue;
                    }
                    ItemStack serverItemStack = ItemModify.clientToServer(clientItemStack);
                    itemStackStructureModifier.write(i, serverItemStack);
                }
            }
        };
    }
}
