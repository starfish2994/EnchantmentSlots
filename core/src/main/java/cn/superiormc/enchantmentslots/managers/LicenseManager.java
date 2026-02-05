package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bstats.bukkit.Metrics;

public final class LicenseManager {

    public static LicenseManager licenseManager;

    public final boolean valid;

    public LicenseManager() {
        licenseManager = this;
        new Metrics(EnchantmentSlots.instance, 23653);
        this.valid = true;
        printStartupInfo();
    }

    private void printStartupInfo() {
        TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " No license found in your jar file. Seems that you are self-building this plugin.");
    }
}
