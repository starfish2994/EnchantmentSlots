package cn.superiormc.enchantmentslots.objects.matchitem;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ContainsName extends AbstractMatchItemRule {
    public ContainsName() {
        super();
    }

    @Override
    public boolean getMatch(ConfigurationSection section, ItemStack item, ItemMeta meta) {
        if (!meta.hasDisplayName()) {
            return false;
        }
        for (String requiredName : section.getStringList("contains-name")) {
            String itemName = EnchantmentSlots.methodUtil.getItemName(meta);
            return TextUtil.clear(itemName).contains(TextUtil.clear(requiredName));
        }
        return false;
    }

    @Override
    public boolean configNotContains(ConfigurationSection section) {
        return section.getStringList("contains-name").isEmpty();
    }
}
