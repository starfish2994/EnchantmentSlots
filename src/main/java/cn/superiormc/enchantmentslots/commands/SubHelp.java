package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.configs.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubHelp {

    public static void SubHelpCommand(CommandSender sender) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Messages.getMessages("help-main-console"));
        } else if (sender.hasPermission("enchantmentslots.admin")) {
            sender.sendMessage(Messages.getMessages("help-main-admin"));
        } else {
            sender.sendMessage(Messages.getMessages("help-main"));
        }
    }
}
