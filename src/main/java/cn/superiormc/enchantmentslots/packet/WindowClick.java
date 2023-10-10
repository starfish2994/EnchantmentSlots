package cn.superiormc.enchantmentslots.packet;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ConfigReader;
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
                    ItemStack serverItemStack;
                    if (EnchantmentSlots.getSavedItem.containsKey(clientItemStack)) {
                        if (ConfigReader.getDebug()) {
                            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fFound item stack in cache.");
                        }
                        serverItemStack = EnchantmentSlots.getSavedItem.get(clientItemStack);
                    }
                    else {
                        ItemMeta itemMeta = clientItemStack.getItemMeta();
                        if (itemMeta == null) {
                            itemMeta = Bukkit.getItemFactory().getItemMeta(clientItemStack.getType());
                        }
                        List<String> lore = new ArrayList<>();
                        int index = -1;
                        if (itemMeta.hasLore()) {
                            lore = itemMeta.getLore();
                            for (int b = 0; b < lore.size(); b++) {
                                String str = lore.get(b);
                                if (ConfigReader.getDebug()) {
                                    Bukkit.getConsoleSender().sendMessage(
                                            "§x§9§8§F§B§9§8[EnchantmentSlots] §fFound WindowsClick lore: " + str);
                                }
                                if (str.contains(EnchantmentSlots.instance.getConfig().getString("messages.add-lore"))) {
                                    if (ConfigReader.getDebug()) {
                                        Bukkit.getConsoleSender().sendMessage(
                                                "§x§9§8§F§B§9§8[EnchantmentSlots] §fRemoved WindowsClick lore.");
                                    }
                                    index = b;
                                    break;
                                }
                            }
                        }
                        if (index >= 0) {
                            lore.remove(index);
                        }
                        serverItemStack = clientItemStack;
                    }
                    itemStackStructureModifier.write(i, serverItemStack);
                }
            }
        };
    }
}
