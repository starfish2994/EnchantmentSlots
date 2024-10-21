package cn.superiormc.enchantmentslots;

import cn.superiormc.enchantmentslots.managers.*;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.protolcol.GeneralProtolcol;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.comphenix.protocol.ProtocolLibrary;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantmentSlots extends JavaPlugin {

    public static EnchantmentSlots instance;

    public static int majorVersion;

    public static int miniorVersion;

    public static boolean isPaper;

    public static boolean newSkullMethod;

    public static boolean eeLegacy = false;

    @Override
    public void onEnable() {
        instance = this;
        try {
            String[] versionParts = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
            majorVersion = versionParts.length > 1 ? Integer.parseInt(versionParts[1]) : 0;
            miniorVersion = versionParts.length > 2 ? Integer.parseInt(versionParts[2]) : 0;
        } catch (Throwable throwable) {
            ErrorManager.errorManager.sendErrorMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cError: Can not get your Minecraft version! Default set to 1.0.0.");
        }
        new ErrorManager();
        new LicenseManager();
        new ConfigManager();
        new HookManager();
        if (CommonUtil.getClass("com.destroystokyo.paper.PaperConfig")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPaper is found, enabled Paper only feature!");
            isPaper = true;
        }
        new ListenerManager();
        new CommandManager();
        if (LicenseManager.licenseManager.checkJarFiles()) {
            GeneralProtolcol.init();
        }
        new LanguageManager();
        if (!CommonUtil.checkClass("com.mojang.authlib.properties.Property", "getValue") && CommonUtil.getMinorVersion(21, 1)) {
            newSkullMethod = true;
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fNew AuthLib found, enabled new skull get method!");
        }
        new Metrics(this, 23653);
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fYour Minecraft version is: 1." + majorVersion + "." + miniorVersion + "!");
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is loaded. Author: PQguanfang.");
    }

    @Override
    public void onDisable() {
        if (ItemModify.lorePrefix.equals("§y")){
            ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        }
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is disabled. Author: PQguanfang.");
    }
}
