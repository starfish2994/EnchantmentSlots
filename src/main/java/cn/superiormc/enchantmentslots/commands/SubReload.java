package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import org.bukkit.command.CommandSender;

public class SubReload {

    public static void SubReloadCommand(CommandSender sender) {
        if (sender.hasPermission("enchantmentslots.admin")) {
            EnchantmentSlots.instance.reloadConfig();
            Messages.init();
            ExtraSlotsItem.init();
            ItemLimits.enchantItems = null;
            ItemLimits.blackItems = null;
            sender.sendMessage(Messages.getMessages("plugin-reloaded"));
        }
        else {
            sender.sendMessage(Messages.getMessages("error-miss-permission"));
        }
    }
}
