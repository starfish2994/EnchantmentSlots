package cn.superiormc.enchantmentslots.protolcol.eco;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import cn.superiormc.enchantmentslots.utils.ItemModify;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.display.Display;
import com.willfp.eco.core.display.DisplayModule;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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
        ItemModify.addLore(player, itemStack, true);
        ItemModify.serverToClient(player, itemStack);
    }
}
