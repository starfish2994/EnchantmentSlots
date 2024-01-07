package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            command.setUsage(null);
            sender.sendMessage(Messages.getMessages("error-args"));
            return false;
        } else {
            sendCommandArg(sender, args);
            return true;
        }
    }

    public void sendCommandArg(CommandSender sender, String[] args){
        if (args[0].equals("help")) {
            SubHelp.SubHelpCommand(sender);
        }
        else if (args[0].equals("reload")) {
            SubReload.SubReloadCommand(sender);
        }
        else if (args[0].equals("setslots")) {
            SubSetSlots.SubSetSlotsCommand(sender, args);
        }
        else if (args[0].equals("giveslots")) {
            SubGiveSlots.SubGiveSlotsCommand(sender, args);
        }
        else
        {
            sender.sendMessage(Messages.getMessages("error-args"));
        }
    }
}
