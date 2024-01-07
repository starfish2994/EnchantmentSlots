package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemModify;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class SetCreativeSlots extends GeneralPackets {

    // 客户端发给服务端
    public SetCreativeSlots() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ConfigReader.getPriority(), PacketType.Play.Client.SET_CREATIVE_SLOT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (ConfigReader.getDebug()) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found SetCreativeSlots packet.");
                }
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                ItemStack clientItemStack = itemStackStructureModifier.read(0);
                if (clientItemStack.getType().isAir()) {
                    return;
                }
                ItemStack serverItemStack = ItemModify.clientToServer(event.getPlayer(), clientItemStack);
                itemStackStructureModifier.write(0, serverItemStack);
            }
        };
    }
}
