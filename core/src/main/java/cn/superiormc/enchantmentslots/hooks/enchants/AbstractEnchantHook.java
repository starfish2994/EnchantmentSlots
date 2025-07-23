package cn.superiormc.enchantmentslots.hooks.enchants;

import cn.superiormc.enchantmentslots.methods.EnchantsUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public abstract class AbstractEnchantHook {

    protected String pluginName;

    public AbstractEnchantHook(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player) {
        return getRawEnchantName(enchantment);
    }

    public abstract String getRawEnchantName(Enchantment enchantment);

    public Map<Enchantment, Integer> sortEnchants(ItemMeta meta) {
        return EnchantsUtil.getEnchantments(meta, false);
    }

    public String getPluginName() {
        return pluginName;
    }
}
