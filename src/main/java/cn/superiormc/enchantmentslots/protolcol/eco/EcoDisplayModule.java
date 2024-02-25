package cn.superiormc.enchantmentslots.protolcol.eco;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
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
        //try {
        //    Class<?> yourClass = Display.class; // 替换为包含 REGISTERED_MODULES 的类
        //    Field field = yourClass.getDeclaredField("REGISTERED_MODULES");
        //    field.setAccessible(true); // 设置私有变量的可访问性
        //    Map<Integer, List<DisplayModule>> registeredModules = (Map<Integer, List<DisplayModule>>) field.get(null);
        //    for (int b : registeredModules.keySet()) {
        //        for (DisplayModule displayModule : registeredModules.get(b)) {
        //            Bukkit.getConsoleSender().sendMessage(displayModule.getPluginName() + "  " + b);
        //        }
        //    }
        //} catch (NoSuchFieldException | IllegalAccessException e) {
        //    e.printStackTrace();
        //}
        if (player == null) {
            return;
        }
        if (itemStack.getType().isAir()) {
            return;
        }
        if (ConfigReader.getAutoAddLore()) {
            ItemModify.addLore(player, itemStack);
        }
        ItemModify.serverToClient(itemStack);
    }
}
