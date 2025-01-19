package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.display.EnchantmentFormattingKt;
import com.willfp.ecoenchants.enchant.EcoEnchantLike;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import com.willfp.libreforge.Holder;
import com.willfp.libreforge.ItemProvidedHolder;
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
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    public static LanguageManager languageManager;

    private YamlConfiguration messageFile;

    private YamlConfiguration tempMessageFile;

    private File file;

    private File tempFile;

    private final Map<Enchantment, String> enchantmentNameCache = new HashMap<>();

    private final Map<Integer, String> enchantmentLevelCache = new HashMap<>();

    public LanguageManager() {
        languageManager = this;
        initLanguage();
    }

    private void initLanguage() {
        file = new File(EnchantmentSlots.instance.getDataFolder() + "/languages/" +
                ConfigManager.configManager.getString("language", "en_US") + ".yml");
        if (!file.exists()) {
            this.file = new File(EnchantmentSlots.instance.getDataFolder(), "message.yml");
            if (!file.exists()) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cWe can not found your message file, " +
                        "please try restart your server!");
            }
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
        } else {
            sendStringText(args);
        }
    }

    public void sendStringText(String... args) {
        String text = this.messageFile.getString(args[0]);
        if (text == null) {
            if (this.tempMessageFile.getString(args[0]) == null) {
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cCan not found language key: " + args[0] + "!");
                return;
            } else {
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
        for (int i = 1; i < args.length; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            } else {
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
            } else {
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
        for (int i = 1; i < args.length; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            } else {
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
            } else {
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
        for (int i = 1; i < args.length; i += 2) {
            String var = "%" + args[i] + "%";
            if (args[i + 1] == null) {
                text = text.replace(var, "");
            } else {
                text = text.replace(var, args[i + 1]);
            }
        }
        return TextUtil.parse(text);
    }

    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player, boolean showTierColor) {
        try {
            if (enchantmentNameCache.containsKey(enchantment)) {
                return enchantmentNameCache.get(enchantment);
            }
            String enchantmentName = ConfigManager.configManager.getString("enchant-name." + enchantment.getKey().getKey(), enchantment.getKey().getKey());
            if (enchantmentName.equals(enchantment.getKey().getKey())) {
                if (CommonUtil.checkPluginLoad("EcoEnchants")) {
                    EcoEnchantLike ecoEnchant = EcoEnchants.INSTANCE.getByID(enchantment.getKey().getKey());
                    if (ecoEnchant != null) {
                        if (showTierColor) {
                            enchantmentName = EcoEnchantsHook.getEcoEnchantName(ecoEnchant, item, player);
                        } else {
                            enchantmentName = StringUtils.format(ecoEnchant.getRawDisplayName());
                        }
                    }
                } else if (CommonUtil.checkPluginLoad("ExcellentEnchants")) {
                    if (!EnchantmentSlots.eeLegacy) {
                        CustomEnchantment excellentEnchant = su.nightexpress.excellentenchants.registry.EnchantRegistry.getByKey(enchantment.getKey());
                        if (excellentEnchant != null) {
                            if (showTierColor) {
                                enchantmentName = NightMessage.asLegacy(excellentEnchant.getFormattedName());
                            } else {
                                enchantmentName = NightMessage.asLegacy(excellentEnchant.getDisplayName());
                            }
                        }
                    } else {
                        EnchantmentData excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
                        if (excellentEnchant != null) {
                            if (showTierColor) {
                                enchantmentName = NightMessage.asLegacy(excellentEnchant.getNameFormatted(-1, excellentEnchant.getCharges(item)).replace(" -1", ""));
                            } else {
                                enchantmentName = NightMessage.asLegacy(excellentEnchant.getName());
                            }
                        }
                    }
                }
            }
            enchantmentNameCache.put(enchantment, TextUtil.parse(enchantmentName));
            return enchantmentName;
        } catch (Throwable throwable) {
            return "ERROR";
        }
    }

    public String getEnchantLevel(int level) {
        if (enchantmentLevelCache.containsKey(level)) {
            return enchantmentLevelCache.get(level);
        }
        String levelName = ConfigManager.configManager.getString("enchant-level." + level, String.valueOf(level));
        enchantmentLevelCache.put(level, levelName);
        return levelName;
    }

}

class EcoEnchantsHook {

    public static String getEcoEnchantName(EcoEnchantLike ecoEnchant, ItemStack item, Player player) {
        Holder holder = EcoEnchants.INSTANCE.getByID(ecoEnchant.getEnchantment().getKey().getKey()).getLevel(item.getEnchantmentLevel(ecoEnchant.getEnchantment()));
        ItemProvidedHolder itemProvidedHolder = new ItemProvidedHolder(holder, item);
        return EnchantmentFormattingKt.getFormattedName(ecoEnchant, 0, itemProvidedHolder.isShowingAnyNotMet(player));
    }
}
