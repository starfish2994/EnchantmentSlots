package cn.superiormc.enchantmentslots.protolcol.ProtocolLib;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class WindowMerchant extends GeneralPackets {

    public WindowMerchant() {
        super();
    }

    @Override
    protected void initPacketAdapter() {
        packetAdapter = new PacketAdapter(EnchantmentSlots.instance, ConfigManager.configManager.getPriority(), PacketType.Play.Server.OPEN_WINDOW_MERCHANT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (ConfigManager.configManager.getBoolean("debug", false)) {
                    Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §f" +
                            "Found OpenWindowMerchant packet.");
                }
                PacketContainer packet = event.getPacket();
                List<MerchantRecipe> list = new ArrayList<>();
                packet.getMerchantRecipeLists().read(0).forEach(recipe -> {
                    ItemStack serverItemStack1 = recipe.getResult();
                    List<ItemStack> serverItemStack2List = recipe.getIngredients();
                    String itemID = HookManager.hookManager.parseItemID(serverItemStack1);
                    int defaultSlot = ConfigManager.configManager.getDefaultLimits(event.getPlayer(), itemID);
                    ItemModify.setSlot(serverItemStack1, defaultSlot, itemID);
                    ItemStack clientItemStack = ItemModify.serverToClient(serverItemStack1, event.getPlayer());
                    MerchantRecipe merchantRecipe = new MerchantRecipe(clientItemStack, recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward(), recipe.getVillagerExperience(), recipe.getPriceMultiplier(), recipe.getDemand(), recipe.getSpecialPrice());
                    for (ItemStack serverItemStack2 : serverItemStack2List) {
                        String itemID2 = HookManager.hookManager.parseItemID(serverItemStack2);
                        int defaultSlot2 = ConfigManager.configManager.getDefaultLimits(event.getPlayer(), itemID2);
                        ItemModify.setSlot(serverItemStack2, defaultSlot2, itemID2);
                        merchantRecipe.addIngredient(ItemModify.serverToClient(serverItemStack2, event.getPlayer()));
                    }
                    list.add(merchantRecipe);
                });

                packet.getMerchantRecipeLists().write(0, list);
            }
        };
    }
}
