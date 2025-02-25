package cn.superiormc.enchantmentslots.hooks.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.nightexpress.excellentenchants.api.enchantment.CustomEnchantment;
import su.nightexpress.nightcore.util.text.NightMessage;

public class EnchantExcellentEnchantsHook extends AbstractEnchantHook {

    public EnchantExcellentEnchantsHook() {
        super("ExcellentEnchants");
    }

    @Override
    public String getRawEnchantName(Enchantment enchantment) {
        CustomEnchantment excellentEnchant = su.nightexpress.excellentenchants.registry.EnchantRegistry.getByKey(enchantment.getKey());
        if (excellentEnchant != null) {
            return NightMessage.asLegacy(excellentEnchant.getFormattedName());
        }
        return null;
    }

    @Override
    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player) {
        CustomEnchantment excellentEnchant = su.nightexpress.excellentenchants.registry.EnchantRegistry.getByKey(enchantment.getKey());
        if (excellentEnchant != null) {
            return NightMessage.asLegacy(excellentEnchant.getDisplayName());
        }
        return null;
    }
}
