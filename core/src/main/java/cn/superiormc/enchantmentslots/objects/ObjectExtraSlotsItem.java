package cn.superiormc.enchantmentslots.objects;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.managers.MatchItemManager;
import cn.superiormc.enchantmentslots.utils.ItemUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class ObjectExtraSlotsItem {

    public static final NamespacedKey ENCHANTMENT_SLOTS_EXTRA = new NamespacedKey(EnchantmentSlots.instance, "enchantment_extra");

    private final String id;

    private double chance;

    private final int addSlot;

    private final ConfigurationSection matchItem;

    private final ConfigurationSection section;

    private final ObjectAction successAction;

    private final ObjectAction failAction;

    private final ObjectCondition condition;

    public ObjectExtraSlotsItem(String id, ConfigurationSection section) {
        this.id = id;
        this.chance = section.getDouble("chance", 100);
        if (chance > 100) {
            chance = 100;
        } else if (chance < 0) {
            chance = 0;
        }
        this.matchItem = section.getConfigurationSection("match-item");
        this.addSlot = section.getInt("add-slots", 1);
        this.successAction = new ObjectAction(section.getConfigurationSection("success-actions"));
        this.failAction = new ObjectAction(section.getConfigurationSection("fail-actions"));
        this.condition = new ObjectCondition(section.getConfigurationSection("conditions"));
        this.section = section;
    }

    public ItemStack getItem(Player player) {
        ConfigurationSection itemSection = section.getConfigurationSection("display-item");
        if (itemSection == null) {
            itemSection = section;
        }
        ItemStack resultItem = ItemUtil.buildItemStack(player, itemSection);
        ItemMeta meta = resultItem.getItemMeta();
        meta.getPersistentDataContainer().set(ENCHANTMENT_SLOTS_EXTRA,
                PersistentDataType.STRING,
                id);
        resultItem.setItemMeta(meta);
        return resultItem;
    }

    public boolean canApply(Player player, ItemStack item) {
        if (!condition.getAllBoolean(player, 1)) {
            return false;
        }
        return MatchItemManager.matchItemManager.getMatch(matchItem, item);
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
        successAction.runAllActions(player, addSlot);
    }

    public void doFailAction(Player player) {
        failAction.runAllActions(player, 0);
    }
}
