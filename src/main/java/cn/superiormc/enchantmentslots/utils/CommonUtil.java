package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean checkPluginLoad(String pluginName){
        return EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled(pluginName);
    }

    public static int getMajorVersion() {
        String version = Bukkit.getVersion();
        Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher(version);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 20;
    }

    public static void dispatchCommand(String command){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void dispatchCommand(Player player, String command){
        Bukkit.dispatchCommand(player, command);
    }

    public static void dispatchOpCommand(Player player, String command){
        boolean playerIsOp = player.isOp();
        try {
            player.setOp(true);
            Bukkit.dispatchCommand(player, command);
        } finally {
            player.setOp(playerIsOp);
        }
    }
}
