package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WindowClick extends GeneralPackets{

    // 客户端发给服务端
    public WindowClick() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ConfigReader.getPriority(), PacketType.Play.Client.WINDOW_CLICK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (ConfigReader.getDebug()) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found WindowsClick packet. Window ID: " + event.getPacket().getIntegers().read(0) + ", " +
                            "Slot: " + event.getPacket().getIntegers().read(2));
                }
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                PacketContainer packet = event.getPacket();
                int windowID = packet.getIntegers().read(0);
                int slot = packet.getIntegers().read(2);
                boolean replaceItem = true;
                if (windowID == 0) {
                    if (slot < 5 || slot > 44) {
                        return;
                    }
                    int spigotSlot;
                    if (slot >= 36) {
                        spigotSlot = slot - 36;
                    } else if (slot <= 8) {
                        spigotSlot = slot + 31;
                    } else {
                        spigotSlot = slot;
                    }
                    ItemStack tempItemStack = event.getPlayer().getInventory().getItem(spigotSlot);
                    if (tempItemStack == null || tempItemStack.getType().isAir()) {
                        tempItemStack = player.getItemOnCursor();
                        if (tempItemStack.getType().isAir()) {
                            return;
                        }
                        replaceItem = false;
                    }
                    ItemStack newItem = ItemModify.addLore(event.getPlayer(), tempItemStack);
                    if (replaceItem && newItem != null && ConfigReader.getAutoAddSlotsLimit()) {
                        if (!player.getItemOnCursor().getType().isAir()) {
                            player.getItemOnCursor().setAmount(0);
                        }
                        event.getPlayer().getInventory().setItem(spigotSlot, newItem);
                    }
                } else {
                    int topSize = player.getOpenInventory().getTopInventory().getSize();
                    if (slot < topSize || slot > topSize + 36) {
                        return;
                    }
                    int spigotSlot;
                    // 如果是最后9个格子
                    if (slot >= 27 + topSize) {
                        spigotSlot = slot - 27 - topSize;
                    // 如果是中间三排
                    } else {
                        spigotSlot = slot - topSize + 9;
                    }
                    ItemStack tempItemStack = event.getPlayer().getInventory().getItem(spigotSlot);
                    if (tempItemStack == null || tempItemStack.getType().isAir()) {
                        tempItemStack = player.getItemOnCursor();
                        if (tempItemStack.getType().isAir()) {
                            return;
                        }
                        replaceItem = false;
                    }
                    ItemStack newItem = ItemModify.addLore(event.getPlayer(), tempItemStack);
                    if (replaceItem && newItem != null && ConfigReader.getAutoAddSlotsLimit()) {
                        if (!player.getItemOnCursor().getType().isAir()) {
                            player.getItemOnCursor().setAmount(0);
                        }
                        event.getPlayer().getInventory().setItem(spigotSlot, newItem);
                    }
                }
            }
        };
    }
}
