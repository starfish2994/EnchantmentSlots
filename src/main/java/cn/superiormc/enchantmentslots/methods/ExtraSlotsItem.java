package cn.superiormc.enchantmentslots.methods;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExtraSlotsItem {

    public static final NamespacedKey ENCHANTMENT_SLOTS_EXTRA = new NamespacedKey(EnchantmentSlots.instance, "enchantment_extra");

    public static Map<String, ExtraSlotsItem> slotsItemMap = new HashMap<>();

    public static void init() {
        ConfigurationSection tempVal1 = EnchantmentSlots.instance.getConfig().getConfigurationSection(
                "add-slot-items");
        if (tempVal1 == null) {
            return;
        }
        slotsItemMap = new HashMap<>();
        for (String key : tempVal1.getKeys(false)) {
            Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[EnchantmentSlots] §fLoaded extra slot item: " + key + "!");
            slotsItemMap.put(key, new ExtraSlotsItem(key, tempVal1.getConfigurationSection(key)));
        }
    }

    public static ItemStack getExtraSlotItem(String itemID) {
        ExtraSlotsItem item = slotsItemMap.get(itemID);
        if (item == null) {
            return null;
        }
        return item.getItem();
    }

    public static ExtraSlotsItem getExtraSlotItemValue(ItemStack item) {
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

    private String id;

    private double chance;

    private int addSlot;

    private List<String> applyItems;

    private List<String> blackItems;

    private ConfigurationSection section;

    public ExtraSlotsItem(String id, ConfigurationSection section) {
        this.id = id;
        this.chance = section.getDouble("chance", 100);
        if (chance > 100) {
            chance = 100;
        } else if (chance < 0) {
            chance = 0;
        }
        this.applyItems = section.getStringList("apply-items");
        this.blackItems = section.getStringList("black-items");
        this.addSlot = section.getInt("add-slots", 1);
        this.section = section;
    }

    public ItemStack getItem() {
        ItemStack resultItem = ItemUtil.buildItemStack(section);
        ItemMeta meta = resultItem.getItemMeta();
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_EXTRA,
                PersistentDataType.STRING,
                id);
        resultItem.setItemMeta(meta);
        return resultItem;
    }

    public boolean canApply(Player player, String itemID) {
        if (!Condition.getBoolean(player, section.getStringList("conditions"))) {
            return false;
        }
        if (!blackItems.isEmpty() && blackItems.contains(itemID)) {
            return false;
        }
        return applyItems.isEmpty() || applyItems.contains("*") || applyItems.contains(itemID);
    }

    public int getAddSlot() {
        Random random = new Random();
        double rollNumber = random.nextDouble() * 100;
        if (chance > rollNumber) {
            return addSlot;
        }
        return 0;
    }

    public void doSuccessAction(Player player) {
        Action.doAction(player, section.getStringList("success-actions"), addSlot);
    }

    public void doFailAction(Player player) {
        Action.doAction(player, section.getStringList("fail-actions"), 0);
    }
}
