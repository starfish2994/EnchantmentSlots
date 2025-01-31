package cn.superiormc.enchantmentslots.objects;

import cn.superiormc.enchantmentslots.managers.MatchItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
                return parseInt(defaultSlotsSection, "default", "5");
            }
            Set<String> groupNameSet = conditionSection.getKeys(false);
            List<Integer> result = new ArrayList<>();
            for (String groupName : groupNameSet) {
                ObjectCondition condition = new ObjectCondition(conditionSection.getConfigurationSection(groupName));
                if (groupName.equals("default") || (defaultSlotsSection.contains(groupName) &&
                        condition.getAllBoolean(player, 0))) {
                    result.add(parseInt(defaultSlotsSection, groupName, "5"));
                }
            }
            if (result.isEmpty()) {
                result.add(parseInt(defaultSlotsSection, "default", "5"));
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
                return parseInt(maxSlotsSection, "default", "-1");
            }
            Set<String> groupNameSet = conditionSection.getKeys(false);
            List<Integer> result = new ArrayList<>();
            for (String groupName : groupNameSet) {
                ObjectCondition condition = new ObjectCondition(conditionSection.getConfigurationSection(groupName));
                if (groupName.equals("default") || (maxSlotsSection.contains(groupName) &&
                        condition.getAllBoolean(player, 0))) {
                    result.add(parseInt(maxSlotsSection, groupName, "-1"));
                }
            }
            if (result.isEmpty()) {
                result.add(parseInt(maxSlotsSection, "default", "-1"));
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

    private int parseInt(ConfigurationSection section, String key, String defaultValue) {
        String input = section.getString(key, defaultValue);
        if (input.contains("~")) {
            // 处理范围的情况
            String[] range = input.split("~");
            int start = Integer.parseInt(range[0].trim());
            int end = Integer.parseInt(range[1].trim());
            if (autoAddLore) {
                return end;
            }
            if (start > end) {
                return start;
            } else {
                Random random = new Random();
                return random.nextInt(end - start + 1) + start;
            }
        } else {
            // 处理纯数字的情况
            return Integer.parseInt(input.trim());
        }
    }
}
