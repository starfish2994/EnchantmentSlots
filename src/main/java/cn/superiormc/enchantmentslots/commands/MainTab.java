package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MainTab implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> strings = new ArrayList<>();
        if (args.length == 1) {
            strings.add("help");
            if (sender.hasPermission("enchantmentslots.reload")) {
                strings.add("reload");
            }
            if (sender.hasPermission("enchantmentslots.setslots")) {
                strings.add("setslots");
            }
            if (sender.hasPermission("enchantmentslots.giveslots")) {
                strings.add("giveslots");
            }
        }
        else if (args.length == 2) {
            if (args[0].equals("setslots")) {
                if (sender.hasPermission("enchantmentslots.setslots")) {
                    strings.add("1");
                    strings.add("5");
                    strings.add("10");
                }
            }
            else if (args[0].equals("giveslots")) {
                if (sender.hasPermission("enchantmentslots.giveslots")) {
                    strings.add("1");
                    strings.add("5");
                    strings.add("10");
                    strings.addAll(ExtraSlotsItem.slotsItemMap.keySet());
                }
            }
        }
        return strings;
    }
}
