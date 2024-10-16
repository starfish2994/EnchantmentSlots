package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.EcoEnchantsHook;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.enchant.EcoEnchantLike;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentenchants.api.enchantment.CustomEnchantment;
import su.nightexpress.excellentenchants.api.enchantment.EnchantmentData;
import su.nightexpress.excellentenchants.enchantment.registry.EnchantRegistry;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class LanguageManager {

    public static LanguageManager languageManager;

    private YamlConfiguration messageFile;

    private YamlConfiguration tempMessageFile;

    private File file;

    private File tempFile;

    public LanguageManager() {
        languageManager = this;
        initLanguage();
    }

    private void initLanguage() {
        file = new File(EnchantmentSlots.instance.getDataFolder() + "/languages/" +
                ConfigManager.configManager.getString("language", "en_US") + ".yml");
        if (!file.exists()){
            File tempVal1 = new File("languages/en_US.yml");
            EnchantmentSlots.instance.saveResource(tempVal1.getPath(), false);
            File tempVal2 = new File("languages/zh_CN.yml");
            EnchantmentSlots.instance.saveResource(tempVal2.getPath(), false);
        }
        messageFile = YamlConfiguration.loadConfiguration(file);
        InputStream is = EnchantmentSlots.instance.getResource("languages/en_US.yml");
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

    public void sendStringText(CommandSender sender, String... args) {
        if (sender instanceof Player) {
            sendStringText((Player) sender, args);
        }
        else {
            sendStringText(args);
        }
    }

    public void sendStringText(String... args) {
        String text = this.messageFile.getString(args[0]);
        if (text == null) {
            if (this.tempMessageFile.getString(args[0]) == null) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found language key: " + args[0] + "!");
                return;
            }
            else {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cUpdated your language file, added " +
                        "new language key and it's default value: " + args[0] + "!");
                text = this.tempMessageFile.getString(args[0]);
                messageFile.set(args[0], text);
                try {
                    messageFile.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (text == null) {
            return;
        }
        for (int i = 1 ; i < args.length ; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            }
            else {
                text = text.replace(var, args[i + 1]);
            }
        }
        if (!text.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(TextUtil.parse(text));
        }
    }

    public void sendStringText(Player player, String... args) {
        String text = this.messageFile.getString(args[0]);
        if (text == null) {
            if (this.tempMessageFile.getString(args[0]) == null) {
                player.sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found language key: " + args[0] + "!");
                return;
            }
            else {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cUpdated your language file, added " +
                        "new language key and it's default value: " + args[0] + "!");
                text = this.tempMessageFile.getString(args[0]);
                messageFile.set(args[0], text);
                try {
                    messageFile.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (text == null) {
            return;
        }
        for (int i = 1 ; i < args.length ; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            }
            else {
                text = text.replace(var, args[i + 1]);
            }
        }
        if (!text.isEmpty()) {
            player.sendMessage(TextUtil.parse(text));
        }
    }

    public String getStringText(String... args) {
        String text = this.messageFile.getString(args[0]);
        if (text == null) {
            if (this.tempMessageFile.getString(args[0]) == null) {
                return "§cCan not found language key: " + args[0] + "!";
            }
            else {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cUpdated your language file, added " +
                        "new language key and it's default value: " + args[0] + "!");
                text = this.tempMessageFile.getString(args[0]);
                messageFile.set(args[0], text);
                try {
                    messageFile.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (text == null) {
            return "";
        }
        for (int i = 1 ; i < args.length ; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            }
            else {
                text = text.replace(var, args[i + 1]);
            }
        }
        return TextUtil.parse(text);
    }

    public static String getEnchantName(ItemStack item, Enchantment enchantment, Player player, boolean showTierColor) {
        try {
            String enchantmentName = ConfigManager.configManager.getString("enchant-name." + enchantment.getKey().getKey(), enchantment.getKey().getKey());
            if (enchantmentName == null) {
                if (CommonUtil.checkPluginLoad("EcoEnchants")) {
                    EcoEnchantLike ecoEnchant = EcoEnchants.INSTANCE.getByID(enchantment.getKey().getKey());
                    if (ecoEnchant != null) {
                        if (showTierColor) {
                            return EcoEnchantsHook.getEcoEnchantName(ecoEnchant, item, player);
                        }
                        return StringUtils.format(ecoEnchant.getRawDisplayName());
                    }
                } else if (CommonUtil.checkPluginLoad("ExcellentEnchants")) {
                    if (!EnchantmentSlots.eeLegacy) {
                        CustomEnchantment excellentEnchant = su.nightexpress.excellentenchants.registry.EnchantRegistry.getByKey(enchantment.getKey());
                        if (excellentEnchant != null) {
                            if (showTierColor) {
                                return NightMessage.asLegacy(excellentEnchant.getFormattedName());
                            }
                            return NightMessage.asLegacy(excellentEnchant.getDisplayName());
                        }
                    } else {
                        EnchantmentData excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
                        if (excellentEnchant != null) {
                            if (showTierColor) {
                                return NightMessage.asLegacy(excellentEnchant.getNameFormatted(-1, excellentEnchant.getCharges(item)).replace(" -1", ""));
                            }
                            return NightMessage.asLegacy(excellentEnchant.getName());
                        }
                    }
                }
            }
            return TextUtil.parse(enchantmentName);
        } catch (Throwable throwable) {
            return "ERROR";
        }
    }
}
