package cn.superiormc.enchantmentslots;

import cn.superiormc.enchantmentslots.hooks.mythicchanger.AddESLore;
import cn.superiormc.enchantmentslots.hooks.mythicchanger.ResetSlot;
import cn.superiormc.enchantmentslots.hooks.mythicchanger.SetSlot;
import cn.superiormc.enchantmentslots.managers.*;
import cn.superiormc.enchantmentslots.methods.ItemModify;
import cn.superiormc.enchantmentslots.papi.PlaceholderAPIExpansion;
import cn.superiormc.enchantmentslots.protolcol.GeneralProtolcol;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.mythicchanger.manager.ChangesManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.loohp.interactivechat.api.InteractiveChatAPI;
import com.loohp.interactivechat.objectholders.ICPlayer;
import com.loohp.interactivechat.objectholders.ICPlayerFactory;
import me.arasple.mc.trchat.module.internal.hook.HookPlugin;
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
        if (CommonUtil.checkPluginLoad("PlaceholderAPI")) {
            PlaceholderAPIExpansion.papi = new PlaceholderAPIExpansion(this);
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into PlaceholderAPI...");
            if (PlaceholderAPIExpansion.papi.register()){
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fFinished hook!");
            }
        }
        if (CommonUtil.checkPluginLoad("InteractiveChat")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into InteractiveChat...");
            InteractiveChatAPI.registerItemStackTransformProvider(EnchantmentSlots.instance, 10, (itemStack, uuid) -> {
                ICPlayer icPlayer = ICPlayerFactory.getICPlayer(uuid);
                return ItemModify.serverToClient(itemStack, icPlayer.getLocalPlayer());
            });
        }
        if (CommonUtil.checkPluginLoad("TrChat")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into TrChat...");
            HookPlugin.INSTANCE.registerDisplayItemHook("EnchantmentSlots", ItemModify::serverToClient);
        }
        if (CommonUtil.checkPluginLoad("ExcellentEnchants") && CommonUtil.getClass("su.nightexpress.excellentenchants.api.enchantment.EnchantmentData")) {
            eeLegacy = true;
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §7Seems that you are using ExcellentEnchants old version, enabled compatibility mode, " +
                    "this mode will be removed in future updates, please consider update it to latest.");
        }
        if (CommonUtil.checkPluginLoad("MythicChanger")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into MythicChanger...");
            ChangesManager.changesManager.registerNewRule(new SetSlot());
            ChangesManager.changesManager.registerNewRule(new ResetSlot());
            ChangesManager.changesManager.registerNewRule(new AddESLore());
        }
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
