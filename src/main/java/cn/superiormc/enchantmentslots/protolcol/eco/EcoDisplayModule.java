package cn.superiormc.enchantmentslots.protolcol.eco;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EcoDisplayModule extends DisplayModule {
    public static void init() {
        Display.registerDisplayModule(new EcoDisplayModule(EcoPlugin.getPlugin("eco"), ConfigReader.getEcoPriority()));
    }

    protected EcoDisplayModule(@NotNull EcoPlugin plugin, @NotNull DisplayPriority priority) {
        super(plugin, priority);
    }

    @Override
    public void display(@NotNull ItemStack itemStack, @Nullable Player player, @NotNull Object... args) {
        if (player == null) {
            return;
        }
        if (itemStack.getType().isAir()) {
            return;
        }
        String itemID = CheckValidHook.checkValid(itemStack);
        int defaultSlot = ConfigReader.getDefaultLimits(player, itemID);
        if (ConfigReader.getAutoAddLore()) {
            ItemModify.addLore(itemStack, defaultSlot, itemID);
        }
        ItemModify.serverToClient(itemStack, player);
    }
}
