package cn.superiormc.enchantmentslots.packet;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import cn.superiormc.enchantmentslots.utils.ItemModify;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.inventory.ItemStack;

public class SetSlots extends GeneralPackets{
    public SetSlots() {
        super();
    }
    @Override
    protected void initPacketAdapter(){
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                for(int i = 0; i < itemStackStructureModifier.size(); i++) {
                    ItemStack serverItemStack = itemStackStructureModifier.read(i);
                    if (serverItemStack.getType().isAir() ||
                            ItemLimits.getMaxEnchantments(serverItemStack) == 0) {
                        continue;
                    }
                    ItemStack clientItemStack = ItemModify.serverToClient(serverItemStack);
                    // client 是加过 Lore 的，server 是没加过的！
                    itemStackStructureModifier.write(i, clientItemStack);
                }
            }
        };
    }
}
