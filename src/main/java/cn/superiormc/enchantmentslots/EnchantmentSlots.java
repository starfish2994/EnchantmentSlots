package cn.superiormc.enchantmentslots;

import cn.superiormc.enchantmentslots.managers.*;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantmentSlots extends JavaPlugin {

    public static EnchantmentSlots instance;

    public static int majorVersion;

    public static int minorVersion;

    public static boolean isPaper;

    public static boolean isFolia = false;

    public static boolean newSkullMethod;

    @Override
    public void onEnable() {
        instance = this;
        try {
            String[] versionParts = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
            majorVersion = versionParts.length > 1 ? Integer.parseInt(versionParts[1]) : 0;
            minorVersion = versionParts.length > 2 ? Integer.parseInt(versionParts[2]) : 0;
        } catch (Throwable throwable) {
            ErrorManager.errorManager.sendErrorMessage("§cError: Can not get your Minecraft version! Default set to 1.0.0.");
        }
        if (CommonUtil.getClass("com.destroystokyo.paper.PaperConfig")) {
            Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fPaper is found, enabled Paper only feature!");
            isPaper = true;
        }
        if (CommonUtil.getClass("io.papermc.paper.threadedregions.RegionizedServerInitEvent")) {
            Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fFolia is found, enabled Folia compatibility feature!");
            Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §6Warning: Folia support is not fully test, major bugs maybe found! " +
                    "Please do not use in production environment!");
            isFolia = true;
        }
        new ErrorManager();
        new InitManager();
        new LicenseManager();
        new ActionManager();
        new ConditionManager();
        new ConfigManager();
        new HookManager();
        new ListenerManager();
        new CommandManager();
        new MatchItemManager();
        new LanguageManager();
        if (!CommonUtil.checkClass("com.mojang.authlib.properties.Property", "getValue") && CommonUtil.getMinorVersion(21, 1)) {
            newSkullMethod = true;
            Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fNew AuthLib found, enabled new skull get method!");
        }
        new Metrics(this, 23653);
        Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fYour Minecraft version is: 1." + majorVersion + "." + minorVersion + "!");
        Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fPlugin is loaded. Author: PQguanfang.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(TextUtil.pluginPrefix() + " §fPlugin is disabled. Author: PQguanfang.");
    }
}
