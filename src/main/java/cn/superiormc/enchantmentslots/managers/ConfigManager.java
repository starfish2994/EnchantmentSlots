package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.objects.ObjectCondition;
import cn.superiormc.enchantmentslots.objects.ObjectExtraSlotsItem;
import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.mythicchanger.manager.MatchItemManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.willfp.eco.core.display.DisplayPriority;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static cn.superiormc.enchantmentslots.objects.ObjectExtraSlotsItem.ENCHANTMENT_SLOTS_EXTRA;

public class ConfigManager {

    public static ConfigManager configManager;

    public FileConfiguration config;

    public Map<String, ObjectExtraSlotsItem> slotsItemMap = new HashMap<>();

    public List<String> enchantItems = null;

    public ConfigManager() {
        configManager = this;
        EnchantmentSlots.instance.saveDefaultConfig();
        this.config = EnchantmentSlots.instance.getConfig();
        initExtraSlotItemConfigs();
    }

    private void initExtraSlotItemConfigs() {
        ConfigurationSection tempVal1 = EnchantmentSlots.instance.getConfig().getConfigurationSection(
                "add-slot-items");
        if (tempVal1 == null) {
            return;
        }
        slotsItemMap = new HashMap<>();
        for (String key : tempVal1.getKeys(false)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fLoaded extra slot item: " + key + "!");
            slotsItemMap.put(key, new ObjectExtraSlotsItem(key, tempVal1.getConfigurationSection(key)));
        }
    }

    public ItemStack getExtraSlotItem(String itemID) {
        ObjectExtraSlotsItem item = slotsItemMap.get(itemID);
        if (item == null) {
            return null;
        }
        return item.getItem();
    }

    public Map<String, ObjectExtraSlotsItem> getSlotsItemMap() {
        return slotsItemMap;
    }

    public ObjectExtraSlotsItem getExtraSlotItemValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(ENCHANTMENT_SLOTS_EXTRA, PersistentDataType.STRING)) {
            return null;
        }
        String id = meta.getPersistentDataContainer().get(ENCHANTMENT_SLOTS_EXTRA, PersistentDataType.STRING);
        return slotsItemMap.get(id);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return config.getBoolean(path, defaultValue);
    }

    public String getString(String path, String... args) {
        String s = config.getString(path);
        if (s == null) {
            if (args.length == 0) {
                return null;
            }
            s = args[0];
        }
        for (int i = 1 ; i < args.length ; i += 2) {
            String var = "{" + args[i] + "}";
            if (args[i + 1] == null) {
                s = s.replace(var, "");
            }
            else {
                s = s.replace(var, args[i + 1]);
            }
        }
        return s.replace("{plugin_folder}", String.valueOf(EnchantmentSlots.instance.getDataFolder()));
    }

    public ListenerPriority getPriority() {
        return ListenerPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.add-lore.packet-listener-priority", EnchantmentSlots.instance.getConfig().getString(
                        "settings.packet-listener-priority", "MONITOR")).toUpperCase());
    }

    public DisplayPriority getEcoPriority() {
        return DisplayPriority.valueOf(EnchantmentSlots.instance.getConfig().getString(
                "settings.add-lore.packet-listener-priority", EnchantmentSlots.instance.getConfig().getString(
                        "settings.packet-listener-priority", "HIGHEST")).toUpperCase());
    }

    public int getDefaultLimits(Player player, String itemID) {
        ConfigurationSection section = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.default-slots-by-item." + itemID);
        if (ConfigManager.configManager.getBoolean("debug", false)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fItem ID: " + itemID);
        }
        if (section == null) {
            section = EnchantmentSlots.instance.getConfig().
                    getConfigurationSection("settings.default-slots");
        }
        ConfigurationSection conditionSection = EnchantmentSlots.instance.getConfig().
                getConfigurationSection("settings.slots-conditions");
        if (section == null) {
            ErrorManager.errorManager.sendErrorMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §cError: " +
                    "Can not found default-slots section, default set to 5!");
            return 5;
        }
        if (conditionSection == null) {
            return section.getInt("default", 5);
        }
        Set<String> groupNameSet = conditionSection.getKeys(false);
        List<Integer> result = new ArrayList<>();
        for (String groupName : groupNameSet) {
            if (groupName.equals("default") || (section.getInt(groupName, -1) != -1 &&
                    ObjectCondition.getBoolean(player, conditionSection.getStringList(groupName)))) {
                result.add(section.getInt(groupName));
            }
        }
        if (result.isEmpty()) {
            if (section.getInt("default", -1) == -1) {
                result.add(5);
            } else {
                result.add(section.getInt("default"));
            }
        }
        return Collections.max(result);
    }

    public int getMaxLimits(Player player, ItemStack itemStack) {
        ConfigurationSection section = config.getConfigurationSection("settings.max-slots-by-item." +
                HookManager.hookManager.parseItemID(itemStack));
        if (section == null) {
            section = config.getConfigurationSection("settings.max-slots");
        }
        ConfigurationSection conditionSection = config.getConfigurationSection("settings.slots-conditions");
        if (section == null) {
            return -1;
        }
        if (conditionSection == null) {
            return section.getInt("default", -1);
        }
        Set<String> groupNameSet = conditionSection.getKeys(false);
        List<Integer> result = new ArrayList<>();
        for (String groupName : groupNameSet) {
            if (groupName.equals("default") || (section.getInt(groupName, -1) != -1 &&
                    ObjectCondition.getBoolean(player, conditionSection.getStringList(groupName)))) {
                result.add(section.getInt(groupName));
            }
        }
        if (result.isEmpty()) {
            result.add(section.getInt("default", -1));
        }
        return Collections.max(result);
    }

    public boolean getOnlyInPlayerInventory(Player player, boolean playerInInventory) {
        if (!config.getBoolean("settings.add-lore.only-in-player-inventory",
                config.getBoolean("settings.only-in-player-inventory", true))) {
            return true;
        }
        InventoryView view = player.getOpenInventory();
        if (view.getType().equals(InventoryType.CHEST)) {
            return playerInInventory || view.getTitle().equals("Chest");
        }
        return playerInInventory || config.getBoolean("settings.add-lore.check-chests-only");
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public List<String> getStringListOrDefault(String originalPath, String newPath) {
        if (config.getStringList(originalPath).isEmpty()) {
            return config.getStringList(newPath);
        }
        return config.getStringList(originalPath);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public boolean canEnchant(ItemStack item, String itemID) {
        if (itemID == null) {
            itemID = "-null";
        }
        ConfigurationSection matchItemSection = getSection("settings.item-can-be-enchanted.match-item");
        if (matchItemSection != null && CommonUtil.checkPluginLoad("MythicChanger")) {
            return MatchItemManager.matchItemManager.getMatch(matchItemSection, item);
        }
        if (enchantItems == null) {
            enchantItems = getStringListOrDefault("settings.item-can-be-enchanted.whitelist-items",
                    "settings.item-can-be-enchanted.match-item.material");
            if (enchantItems.isEmpty()) {
                enchantItems = new ArrayList<>();
            }
        }
        if (!enchantItems.isEmpty()) {
            for (String tempVal1 : enchantItems) {
                if (tempVal1.equalsIgnoreCase(item.getType().name()) || tempVal1.equalsIgnoreCase(itemID)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean canDisplay(ItemStack item) {
        boolean isBook = ConfigManager.configManager.getBoolean("settings.add-lore.black-book",
                true) && (item.getType().equals(Material.BOOK) || item.getType().equals(Material.ENCHANTED_BOOK));
        ConfigurationSection matchItemSection = getSection("settings.add-lore.match-item");
        if (matchItemSection != null && CommonUtil.checkPluginLoad("MythicChanger")) {
            return !isBook && MatchItemManager.matchItemManager.getMatch(matchItemSection, item);
        }
        return !isBook;
    }

}
