package cn.superiormc.enchantmentslots.utils;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.ConfigManager;
import cn.superiormc.enchantmentslots.managers.ErrorManager;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Enums;
import com.google.common.collect.MultimapBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.willfp.ecoenchants.display.EnchantSorter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentenchants.api.enchantment.EnchantmentData;
import su.nightexpress.excellentenchants.enchantment.util.EnchantUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ItemUtil {
    
    public static ItemStack buildItemStack(ConfigurationSection section) {
        ItemStack item = new ItemStack(Material.STONE);
        String materialKey = section.getString("material");
        if (materialKey != null) {
            Material material = Material.getMaterial(materialKey.toUpperCase());
            if (material != null) {
                item.setType(material);
            }
        }
        int amountKey = section.getInt("amount", -1);
        if (amountKey > 0) {
            item.setAmount(amountKey);
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        String displayNameKey = section.getString("name");
        if (displayNameKey != null) {
            meta.setDisplayName(TextUtil.parse(displayNameKey));
        }
        List<String> loreKey = section.getStringList("lore");
        if (!loreKey.isEmpty()) {
            meta.setLore(TextUtil.getListWithColor(loreKey));
        }
        if (CommonUtil.getMajorVersion(14)) {
            int customModelDataKey = section.getInt("custom-model-data", section.getInt("cmd", -1));
            if (customModelDataKey > 0) {
                meta.setCustomModelData(customModelDataKey);
            }
        }
        List<String> itemFlagKey = section.getStringList("flags");
        if (!itemFlagKey.isEmpty()) {
            for (String flag : itemFlagKey) {
                flag = flag.toUpperCase();
                ItemFlag itemFlag = Enums.getIfPresent(ItemFlag.class, flag).orNull();
                if (itemFlag != null) {
                    meta.addItemFlags(itemFlag);
                }
                if (CommonUtil.getMinorVersion(20, 6) && itemFlag == ItemFlag.HIDE_ATTRIBUTES && meta.getAttributeModifiers() == null) {
                    meta.setAttributeModifiers(MultimapBuilder.hashKeys().hashSetValues().build());
                }
            }
        }
        ConfigurationSection enchantsKey = section.getConfigurationSection("enchants");
        if (enchantsKey != null) {
            for (String ench : enchantsKey.getKeys(false)) {
                Enchantment vanillaEnchant = Enchantment.getByKey(NamespacedKey.minecraft(ench.toLowerCase()));
                if (vanillaEnchant != null) {
                    meta.addEnchant(vanillaEnchant, enchantsKey.getInt(ench), true);
                }
            }
        }
        // Skull
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            String skullTextureNameKey = section.getString("skull-meta", section.getString("skull"));
            if (skullTextureNameKey != null) {
                if (skullTextureNameKey.length() > 16) {
                    if (EnchantmentSlots.isPaper && ConfigManager.configManager.getBoolean("paper-api.skull", false)) {
                        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
                        profile.setProperty(new ProfileProperty("textures", skullTextureNameKey));
                        skullMeta.setPlayerProfile(profile);
                    } else {
                        if (EnchantmentSlots.newSkullMethod) {
                            try {
                                Class<?> profileClass = Class.forName("net.minecraft.world.item.component.ResolvableProfile");
                                Constructor<?> constroctor = profileClass.getConstructor(GameProfile.class);
                                GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                                profile.getProperties().put("textures", new Property("textures", skullTextureNameKey));
                                try {
                                    Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", profileClass);
                                    mtd.setAccessible(true);
                                    mtd.invoke(skullMeta, constroctor.newInstance(profile));
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    ErrorManager.errorManager.sendErrorMessage("§x§9§8§F§B§9§8[ManyouItems] §cError: Can not parse skull texture in a item!");
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                            profile.getProperties().put("textures", new Property("textures", skullTextureNameKey));
                            try {
                                Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                                mtd.setAccessible(true);
                                mtd.invoke(skullMeta, profile);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                ErrorManager.errorManager.sendErrorMessage("§x§9§8§F§B§9§8[ManyouItems] §cError: Can not parse skull texture in a item!");
                            }
                        }
                    }
                } else {
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(skullTextureNameKey));
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isValid(ItemStack item) {
        return item != null && !item.getType().isAir();
    }
}
