package cn.superiormc.enchantmentslots.configs;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ColorParser;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.enchant.EcoEnchant;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import su.nexmedia.engine.utils.Colorizer;
import su.nightexpress.excellentenchants.enchantment.impl.ExcellentEnchant;
import su.nightexpress.excellentenchants.enchantment.registry.EnchantRegistry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Messages {

    private static YamlConfiguration messageFile;

    private static YamlConfiguration tempMessageFile;

    private static File file;

    private static File tempFile;

    public static void init() {
        file = new File(EnchantmentSlots.instance.getDataFolder(), "message.yml");
        if (!file.exists()){
            File tempVal1 = new File("message.yml");
            EnchantmentSlots.instance.saveResource(tempVal1.getPath(), false);
        }
        messageFile = YamlConfiguration.loadConfiguration(file);
        InputStream is = EnchantmentSlots.instance.getResource("message.yml");
        if (is == null) {
            return;
        }
        tempFile = new File(EnchantmentSlots.instance.getDataFolder(), "tempMessage.yml");
        try {
            Files.copy(is, tempFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempMessageFile = YamlConfiguration.loadConfiguration(tempFile);
        tempFile.delete();
    }

    public static String getMessages(String path) {
        if (messageFile.getString(path) == null) {
            if (tempMessageFile.getString(path) == null) {
                return "§cCan not found language key: " + path + "!";
            }
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cUpdated your language file, added " +
                    "new language key and it's default value: " + path + "!");
            messageFile.set(path, tempMessageFile.getString(path));
            try {
                messageFile.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return TextUtil.parse(tempMessageFile.getString(path));
        }
        return TextUtil.parse(messageFile.getString(path));
    }

    public static String getEnchantName(Enchantment enchantment, boolean showTierColor) {
        if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("EcoEnchants")) {
            try {
                if (Integer.valueOf(EnchantmentSlots.instance.getServer().getPluginManager().getPlugin("EcoEnchants").getDescription().
                        getVersion().split("\\.")[0]) > 10) {
                    EcoEnchant ecoEnchant = EcoEnchants.INSTANCE.getByID(enchantment.getKey().getKey());
                    if (ecoEnchant != null) {
                        String name = ecoEnchant.getRawDisplayName();
                        if (showTierColor) {
                            name = ecoEnchant.getType().getFormat() + name;
                        }
                        return StringUtils.format(name);
                    }
                } else {
                    com.willfp.ecoenchants.enchants.EcoEnchant ecoEnchant = com.willfp.ecoenchants.enchants.EcoEnchants.getByKey(enchantment.getKey());
                    if (ecoEnchant != null) {
                        return ecoEnchant.getDisplayName();
                    }
                }
            } catch (Exception ep) {
                return TextUtil.parse(ConfigReader.getEnchantmentName(enchantment));
            }
        } else if (EnchantmentSlots.instance.getServer().getPluginManager().isPluginEnabled("ExcellentEnchants")) {
            ExcellentEnchant excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
            if (excellentEnchant != null) {
                String name = excellentEnchant.getDisplayName();
                if (showTierColor) {
                    name = excellentEnchant.getTier().getColor() + name;
                }
                return Colorizer.apply(name);
            }
        }
        return TextUtil.parse(ConfigReader.getEnchantmentName(enchantment));
    }
}
