package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.methods.AddLore;
import cn.superiormc.enchantmentslots.methods.SlotUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

// 服务端发给客户端
public class WindowItem extends GeneralPackets {

    public WindowItem() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ConfigManager.configManager.getPriority(), PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (ConfigManager.configManager.getBoolean("debug", false)) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found WindowsItem packet. Window ID: " + event.getPacket().getIntegers().read(0));
                }
                if (event.getPlayer() == null) {
                    return;
                }
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> singleItemStackStructureModifier = packet.getItemModifier();
                if (singleItemStackStructureModifier.size() != 0) {
                    ItemStack serverItemStack = singleItemStackStructureModifier.read(0);
                    if (ConfigManager.configManager.isAutoAddLore(serverItemStack, event.getPlayer(), true)) {
                        SlotUtil.setSlot(serverItemStack, event.getPlayer(), false);
                    }
                    ItemStack clientItemStack = AddLore.addLore(serverItemStack, event.getPlayer());
                    // client 是加过 Lore 的，server 是没加过的！
                    singleItemStackStructureModifier.write(0, clientItemStack);
                }
                StructureModifier<List<ItemStack>> itemStackStructureModifier = packet.getItemListModifier();
                List<ItemStack> serverItemStack = itemStackStructureModifier.read(0);
                List<ItemStack> clientItemStack = new ArrayList<>();
                int index = 1;
                for (ItemStack itemStack : serverItemStack) {
                    if (itemStack.getType().isAir()) {
                        clientItemStack.add(itemStack);
                        continue;
                    }
                    boolean isPlayerInventory = event.getPacket().getIntegers().read(0) == 0 || index > serverItemStack.size() - 36;
                    if (ConfigManager.configManager.isAutoAddLore(itemStack, event.getPlayer(), isPlayerInventory)) {
                        SlotUtil.setSlot(itemStack, event.getPlayer(), isPlayerInventory);
                    }
                    clientItemStack.add(AddLore.addLore(itemStack, event.getPlayer()));
                    index ++;
                }
                // client 是加过 Lore 的，server 是没加过的！
                itemStackStructureModifier.write(0, clientItemStack);
            }
        };
    }
}
