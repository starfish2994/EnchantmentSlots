package cn.superiormc.enchantmentslots;

import cn.superiormc.enchantmentslots.commands.MainCommand;
import cn.superiormc.enchantmentslots.commands.MainTab;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.listeners.*;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.papi.PlaceholderAPIExpansion;
import cn.superiormc.enchantmentslots.protolcol.GeneralProtolcol;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public final class EnchantmentSlots extends JavaPlugin {

    public static EnchantmentSlots instance;

    public static String getUser = "%%__USER__%%";

    public static String getUserName = "%%__USERNAME__%%";

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        if (CommonUtil.checkJarFiles()) {
            GeneralProtolcol.init();
            ExtraSlotsItem.init();
        }
        Messages.init();
        if (CommonUtil.checkPluginLoad("PlaceholderAPI")) {
            PlaceholderAPIExpansion.papi = new PlaceholderAPIExpansion(this);
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fHooking into PlaceholderAPI...");
            if (PlaceholderAPIExpansion.papi.register()){
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fFinished hook!");
            }
        }
        if (getUserName.equals("%%__USERNAME__%%")) {
            checkPurchase(getUser);
        }
        if (!getUserName.isEmpty() && !getUserName.contains("%")) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fLicense to: " + getUserName + ".");
        }
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is loaded. Author: PQguanfang.");
    }

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fPlugin is disabled. Author: PQguanfang.");
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerEnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerAnvilListener(), this);
        if (EnchantmentSlots.instance.getConfig().getBoolean("settings.set-slot-trigger.InventoryClickEvent.enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new PlayerClickListener(), this);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), this);
        if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("EnchantGui")) {
            Bukkit.getPluginManager().registerEvents(new EnchantGUIEnchantListener(), this);
        }
    }

    public void registerCommands() {
        Objects.requireNonNull(Bukkit.getPluginCommand("enchantmentslots")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("enchantmentslots")).setTabCompleter(new MainTab());
    }

    public static void checkPurchase(String user) {
        if (user.equals("%%__USER__%%")) {
            return;
        }
        String url = "https://api.spigotmc.org/simple/0.2/index.php?action=getAuthor&id=" + user;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String jsonString = response.toString();
                String username = "";
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    username = jsonObject.getString("username");
                } catch (JSONException ignored) {
                }
                getUserName = username;
            }
            connection.disconnect();
        } catch (IOException ignored) {
        }
    }
}
