package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.utils.ConfigReader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class SubReload {

    public static void SubReloadCommand(CommandSender sender)
    {
        if(sender.hasPermission("enchantmentslots.admin")) {
            EnchantmentSlots.instance.reloadConfig();
            sender.sendMessage(ConfigReader.getMessages("plugin-reloaded"));
        }
        else{
            sender.sendMessage(ConfigReader.getMessages("error-miss-permission"));
        }
    }
}
