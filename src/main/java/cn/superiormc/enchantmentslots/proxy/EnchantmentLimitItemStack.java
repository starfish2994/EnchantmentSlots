package cn.superiormc.enchantmentslots.proxy;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantmentLimitItemStack extends ItemStack {

    public EnchantmentLimitItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        if (enchantments.size() > ItemLimits.getMaxEnchantments(this)) {
            throw new IllegalArgumentException("无法添加这么多附魔！");
        }
        super.addEnchantments(enchantments);
    }
}