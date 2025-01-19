package cn.superiormc.enchantmentslots.objects;

import cn.superiormc.enchantmentslots.managers.MatchItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ObjectItemSlot {

    private final String id;

    private final ConfigurationSection matchItemSection;

    private final ConfigurationSection defaultSlotsSection;

    private final ConfigurationSection maxSlotsSection;

    private final ConfigurationSection conditionSection;

    private final boolean autoAddLore;

    public ObjectItemSlot(String id, ConfigurationSection section) {
        this.id = id;
        this.matchItemSection = section.getConfigurationSection("match-item");
        this.defaultSlotsSection = section.getConfigurationSection("default-slots");
        this.maxSlotsSection = section.getConfigurationSection("max-slots");
        this.conditionSection = section.getConfigurationSection("slots-conditions");
        this.autoAddLore = section.getBoolean("auto-add-lore", false);
    }

    public int getDefaultSlot(ItemStack item, Player player) {
        if (matchItemSection == null || MatchItemManager.matchItemManager.getMatch(matchItemSection, item)) {
            if (defaultSlotsSection == null) {
                return -1;
            }
            if (conditionSection == null) {
                return defaultSlotsSection.getInt("default", 5);
            }
            Set<String> groupNameSet = conditionSection.getKeys(false);
            List<Integer> result = new ArrayList<>();
            for (String groupName : groupNameSet) {
                ObjectCondition condition = new ObjectCondition(conditionSection.getConfigurationSection(groupName));
                if (groupName.equals("default") || (defaultSlotsSection.getInt(groupName, -1) != -1 &&
                        condition.getAllBoolean(player, 0))) {
                    result.add(defaultSlotsSection.getInt(groupName));
                }
            }
            if (result.isEmpty()) {
                if (defaultSlotsSection.getInt("default", -1) == -1) {
                    result.add(5);
                } else {
                    result.add(defaultSlotsSection.getInt("default"));
                }
            }
            return Collections.max(result);
        }
        return -1;
    }

    public int getMaxSlot(ItemStack item, Player player) {
        if (matchItemSection == null || MatchItemManager.matchItemManager.getMatch(matchItemSection, item)) {
            if (matchItemSection == null) {
                return -1;
            }
            if (conditionSection == null) {
                return maxSlotsSection.getInt("default", -1);
            }
            Set<String> groupNameSet = conditionSection.getKeys(false);
            List<Integer> result = new ArrayList<>();
            for (String groupName : groupNameSet) {
                ObjectCondition condition = new ObjectCondition(conditionSection.getConfigurationSection(groupName));
                if (groupName.equals("default") || (maxSlotsSection.getInt(groupName, -1) != -1 &&
                        condition.getAllBoolean(player, 0))) {
                    result.add(maxSlotsSection.getInt(groupName));
                }
            }
            if (result.isEmpty()) {
                result.add(maxSlotsSection.getInt("default", -1));
            }
            return Collections.max(result);
        }
        return -1;
    }

    public boolean isAutoAddLore(ItemStack item) {
        if (!MatchItemManager.matchItemManager.getMatch(matchItemSection, item)) {
            return false;
        }
        return autoAddLore;
    }
}
