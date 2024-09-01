package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean checkPluginLoad(String pluginName) {
        if (pluginName.equals("EcoEnchants") && EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            return Integer.parseInt(EnchantmentSlots.instance.getServer().getPluginManager().getPlugin("EcoEnchants").getDescription().
                    getVersion().split("\\.")[0]) > 10;
        }
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

    public static boolean inPlayerInventory(Player player, int slot) {
        int topSize = player.getOpenInventory().getTopInventory().getSize();
        if (player.getOpenInventory().getTopInventory() instanceof CraftingInventory &&
        player.getOpenInventory().getTopInventory().getSize() == 5) {
            return slot >= 5 && slot <= 44;
        }
        return slot >= topSize;
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

    public static String modifyString(String text, String... args) {
        for (int i = 0 ; i < args.length ; i += 2) {
            String var1 = "{" + args[i] + "}";
            String var2 = "[" + args[i] + "]";
            if (args[i + 1] == null) {
                text = text.replace(var1, "").replace(var2, "");
            }
            else {
                text = text.replace(var1, args[i + 1]).replace(var2, args[i + 1]);
            }
        }
        return text;
    }

    public static boolean checkJarFiles() {
        try {
            boolean txtFileFound = false;
            String jarFilePath = CommonUtil.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            JarFile jarFile = new JarFile(jarFilePath);
            String comment = jarFile.getComment();
            if (comment != null && !comment.isEmpty()) {
                if (comment.toUpperCase().contains("LEAK") ||
                        comment.toUpperCase().contains("BLACK") ||
                        comment.toUpperCase().contains("NULLED") ||
                        comment.toUpperCase().contains("RESOURCE")) {
                    return false;
                }
            }
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFilePath));
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                // 获取条目的名称
                String entryName = entry.getName();
                // 判断是否为第一层的文件（不包含文件夹）
                if (!entryName.contains("/") && entryName.toLowerCase().endsWith(".txt")) {
                    txtFileFound = true;
                    break;
                }
            }
            jarInputStream.close();
            return !txtFileFound;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean getClass(String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
}
