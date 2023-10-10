package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            command.setUsage(null);
            sender.sendMessage(ConfigReader.getMessages("error-args"));
            return false;
        } else {
            sendCommandArg(sender, command, label, args);
            return true;
        }
    }

    public void sendCommandArg(CommandSender sender, Command command, String label, String[] args){
        if (args[0].equals("help")) {
            SubHelp.SubHelpCommand(sender);
        }
        else if (args[0].equals("list")) {
            //SubList.SubListCommand(sender);
        }
        else if (args[0].equals("reload")) {
            SubReload.SubReloadCommand(sender);
        }
        else if (args[0].equals("save")) {
            //SubSave.SubSaveCommand(sender, args);
        }
        else
        {
            sender.sendMessage(ConfigReader.getMessages("error-args"));
        }
    }
}
