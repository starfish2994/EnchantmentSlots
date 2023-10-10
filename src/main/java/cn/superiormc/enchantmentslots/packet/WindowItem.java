package cn.superiormc.enchantmentslots.packet;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

// 服务端发给客户端
public class WindowItem extends GeneralPackets{

    public WindowItem() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                for(int i = 0; i < itemStackStructureModifier.size(); i++) {
                    ItemStack serverItemStack = itemStackStructureModifier.read(i);
                    ItemStack clientItemStack = serverItemStack.clone();
                    if (clientItemStack != null && !EnchantmentSlots.getCheckingItem.contains(clientItemStack)) {
                        EnchantmentSlots.getCheckingItem.add(clientItemStack);
                        if (EnchantmentSlots.getSavedItem.containsKey(clientItemStack)) {
                            EnchantmentSlots.getCheckingItem.remove(clientItemStack);
                            return;
                        } else {
                            if (!clientItemStack.hasItemMeta()) continue;
                            ItemMeta itemMeta = clientItemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            if (ConfigReader.getDebug()) {
                                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fSent a WindowsItem packet.");
                            }
                            lore.add(ConfigReader.getMessages("add-lore")
                                    .replace("%amount%", String.valueOf(ItemLimits.getMaxEnchantments(serverItemStack))));
                            itemMeta.setLore(lore);
                            clientItemStack.setItemMeta(itemMeta);
                            // client 是加过 Lore 的，server 是没加过的！
                            itemStackStructureModifier.write(i, clientItemStack);
                            EnchantmentSlots.getSavedItem.put(clientItemStack, serverItemStack);
                            EnchantmentSlots.getCheckingItem.remove(clientItemStack);
                        }
                    }
                }
            }
        };
    }
}
