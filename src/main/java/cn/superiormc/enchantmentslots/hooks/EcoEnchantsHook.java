package cn.superiormc.enchantmentslots.hooks;

import com.willfp.ecoenchants.display.EnchantmentFormattingKt;
import com.willfp.ecoenchants.enchant.EcoEnchantLike;
import com.willfp.ecoenchants.enchant.EcoEnchants;
import com.willfp.libreforge.Holder;
import com.willfp.libreforge.ItemProvidedHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EcoEnchantsHook {

    public static String getEcoEnchantName(EcoEnchantLike ecoEnchant, ItemStack item, Player player) {
        Holder holder = EcoEnchants.INSTANCE.getByID(ecoEnchant.getEnchantment().getKey().getKey()).getLevel(item.getEnchantmentLevel(ecoEnchant.getEnchantment()));
        ItemProvidedHolder itemProvidedHolder = new ItemProvidedHolder(holder, item);
        return EnchantmentFormattingKt.getFormattedName(ecoEnchant, 0, itemProvidedHolder.isShowingAnyNotMet(player));
    }
}
