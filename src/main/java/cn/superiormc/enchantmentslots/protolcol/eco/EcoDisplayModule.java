package cn.superiormc.enchantmentslots.protolcol.eco;

import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.HookManager;
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
        Display.registerDisplayModule(new EcoDisplayModule(EcoPlugin.getPlugin("eco"),
                ConfigManager.configManager.getEcoPriority()));
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
        String itemID = HookManager.hookManager.parseItemID(itemStack);
        int defaultSlot = ConfigManager.configManager.getDefaultLimits(player, itemID);
        if (ConfigManager.configManager.getBoolean("settings.item-can-be-enchanted.auto-add-lore", false)) {
            ItemModify.setSlot(itemStack, defaultSlot, itemID);
        }
        ItemModify.serverToClient(itemStack, player);
    }
}
