package cn.superiormc.enchantmentslots.hooks.enchants;

import cc.polarastrum.aiyatsbus.core.Aiyatsbus;
import cc.polarastrum.aiyatsbus.core.AiyatsbusEnchantment;
import cc.polarastrum.aiyatsbus.core.AiyatsbusUtilsKt;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class EnchantAiyatsbusHook extends AbstractEnchantHook {

    public EnchantAiyatsbusHook() {
        super("Aiyatsbus");
    }

    @Override
    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player) {
        AiyatsbusEnchantment aiyatsbusEnchantment = Aiyatsbus.INSTANCE.api().getEnchantmentManager().getEnchant(enchantment.getKey());
        if (aiyatsbusEnchantment == null) {
            return null;
        }
        return aiyatsbusEnchantment.getRarity().displayName(aiyatsbusEnchantment.getBasicData().getName());
    }

    @Override
    public String getRawEnchantName(Enchantment enchantment) {
        AiyatsbusEnchantment aiyatsbusEnchantment = Aiyatsbus.INSTANCE.api().getEnchantmentManager().getEnchant(enchantment.getKey());
        if (aiyatsbusEnchantment == null) {
            return null;
        }
        return aiyatsbusEnchantment.getBasicData().getName();
    }

    @Override
    public Map<Enchantment, Integer> sortEnchants(ItemMeta meta) {
        // 为什么 AiyatsbusEnchantment 不直接 extend BukkitAPI 的 Enchantment？
        Map<AiyatsbusEnchantment, Integer> sort = Aiyatsbus.INSTANCE.api().getDisplayManager().sortEnchants(AiyatsbusUtilsKt.getFixedEnchants(meta));
        Map<Enchantment, Integer> result = new HashMap<>();
        for (AiyatsbusEnchantment aEnch : sort.keySet()) {
            result.put(aEnch.getEnchantment(), sort.get(aEnch));
        }
        return result;
    }
}
