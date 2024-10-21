package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetSlots extends GeneralPackets {
    public SetSlots() {
        super();
    }
    @Override
    protected void initPacketAdapter(){
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ConfigManager.configManager.getPriority(), PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (ConfigManager.configManager.getBoolean("debug", false)) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found SetSlots packet.");
                }
                if (event.getPlayer() == null) {
                    return;
                }
                PacketContainer packet = event.getPacket();
                int windowID = packet.getIntegers().read(0);
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                ItemStack serverItemStack = itemStackStructureModifier.read(0);
                if (serverItemStack == null || serverItemStack.getType().isAir()) {
                    return;
                }
                int slot = packet.getIntegers().read(packet.getIntegers().size() - 1);
                int spigotSlot;
                if (slot >= 36) {
                    spigotSlot = slot - 36;
                } else if (slot <= 8) {
                    spigotSlot = slot + 31;
                } else {
                    spigotSlot = slot;
                }
                if (ConfigManager.configManager.getBoolean("debug", false)) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Packet Slot ID: " + slot + ", Window ID: " + windowID + ", Top Size: " +
                            event.getPlayer().getOpenInventory().getTopInventory().getSize() + ".");
                }
                boolean dontAddLore = false;
                if (CommonUtil.inPlayerInventory(event.getPlayer(), slot) && (ConfigManager.configManager.getBoolean(
                        "settings.set-slot-trigger.SetSlotPacket.enabled", true)) ||
                        ConfigManager.configManager.getBoolean(
                                "settings.set-slot-trigger.SetSlotPacket.remove-illegal-excess-enchant",
                                true)) {
                    ItemStack targetItem = event.getPlayer().getInventory().getItem(spigotSlot);
                    if (ConfigManager.configManager.getBoolean(
                            "settings.set-slot-trigger.SetSlotPacket.enabled", true)) {
                        if (targetItem != null && !targetItem.getType().isAir()) {
                            boolean isBook = ConfigManager.configManager.getBoolean("settings.set-slot-trigger.black-book",
                                    true) && (targetItem.getType().equals(Material.BOOK) || targetItem.getType().equals(Material.ENCHANTED_BOOK));
                            if (!isBook) {
                                String itemID = HookManager.hookManager.parseItemID(targetItem);
                                int defaultSlot = ConfigManager.configManager.getDefaultLimits(event.getPlayer(), itemID);
                                ItemModify.setSlot(targetItem, defaultSlot, itemID);
                                dontAddLore = true;
                            }
                        }
                    }
                    if (ConfigManager.configManager.getBoolean("settings.set-slot-trigger.SetSlotPacket.remove-illegal-excess-enchant", true)) {
                        if (targetItem != null && !targetItem.getType().isAir()) {
                            int maxEnchantments = ItemLimits.getRealMaxEnchantments(serverItemStack);
                            if (maxEnchantments > 0 && targetItem.getEnchantments().size() > maxEnchantments) {
                                int removeAmount = targetItem.getEnchantments().size() - maxEnchantments;
                                for (Enchantment enchant : targetItem.getEnchantments().keySet()) {
                                    if (removeAmount <= 0) {
                                        break;
                                    }
                                    ItemMeta meta = targetItem.getItemMeta();
                                    if (meta == null) {
                                        break;
                                    }
                                    meta.removeEnchant(enchant);
                                    targetItem.setItemMeta(meta);
                                    removeAmount--;
                                }
                            }
                        }
                    }
                }
                if (!dontAddLore && ConfigManager.configManager.getBoolean("settings.item-can-be-enchanted.auto-add-lore", false) &&
                        ConfigManager.configManager.getOnlyInPlayerInventory(event.getPlayer(),
                        CommonUtil.inPlayerInventory(event.getPlayer(), slot))) {
                    String itemID = HookManager.hookManager.parseItemID(serverItemStack);
                    int defaultSlot = ConfigManager.configManager.getDefaultLimits(event.getPlayer(), itemID);
                    ItemModify.setSlot(serverItemStack, defaultSlot, itemID);
                }
                ItemStack clientItemStack = ItemModify.serverToClient(serverItemStack, event.getPlayer());
                // client 是加过 Lore 的，server 是没加过的！
                itemStackStructureModifier.write(0, clientItemStack);
            }
        };
    }
}
