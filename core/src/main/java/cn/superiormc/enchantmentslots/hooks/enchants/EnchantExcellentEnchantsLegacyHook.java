package cn.superiormc.enchantmentslots.hooks.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import su.nightexpress.excellentenchants.api.enchantment.EnchantmentData;
import su.nightexpress.excellentenchants.enchantment.registry.EnchantRegistry;
import su.nightexpress.excellentenchants.enchantment.util.EnchantUtils;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantExcellentEnchantsLegacyHook extends AbstractEnchantHook {

    public EnchantExcellentEnchantsLegacyHook() {
        super("ExcellentEnchants");
    }

    @Override
    public String getRawEnchantName(Enchantment enchantment) {
        EnchantmentData excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
        if (excellentEnchant != null) {
            return NightMessage.asLegacy(excellentEnchant.getName());
        }
        return enchantment.getKey().asString();
    }

    @Override
    public String getEnchantName(ItemStack item, Enchantment enchantment, Player player) {
        EnchantmentData excellentEnchant = EnchantRegistry.getByKey(enchantment.getKey());
        if (excellentEnchant != null) {
            NightMessage.asLegacy(excellentEnchant.getNameFormatted(-1, excellentEnchant.getCharges(item)).replace(" -1", ""));
        }
        return null;
    }

    @Override
    public Map<Enchantment, Integer> sortEnchants(ItemMeta meta) {
        Map<Enchantment, Integer> itemEnchants = EnchantUtils.getCustomEnchantments(meta)
                .entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<EnchantmentData, Integer> entry) -> entry.getKey().getRarity().getWeight())
                        .thenComparing(entry -> entry.getKey().getName()))
                .collect(Collectors.toMap(entry -> entry.getKey().getEnchantment(), Map.Entry::getValue, (old, newv) -> newv, LinkedHashMap::new));
        Map<Enchantment, Integer> enchantments;
        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            enchantments = storageMeta.getStoredEnchants();
            if (enchantments.isEmpty()) {
                enchantments = meta.getEnchants();
            }
        } else {
            enchantments = meta.getEnchants();
        }
        for (Enchantment singleEnch : enchantments.keySet()) {
            if (!itemEnchants.containsKey(singleEnch)) {
                itemEnchants.put(singleEnch, enchantments.get(singleEnch));
            }
        }
        return itemEnchants;
    }

}
