package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubHelp {

    public static void SubHelpCommand(CommandSender sender) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ConfigReader.getMessages("help-main-console"));
        } else if (sender.hasPermission("enchantmentslots.admin")) {
            sender.sendMessage(ConfigReader.getMessages("help-main-admin"));
        } else {
            sender.sendMessage(ConfigReader.getMessages("help-main"));
        }
    }
}
