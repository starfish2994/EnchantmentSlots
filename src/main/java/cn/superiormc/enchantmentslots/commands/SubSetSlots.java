package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SubSetSlots {

    public static void SubSetSlotsCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Messages.getMessages("error-in-game"));
        } else if (sender.hasPermission("enchantmentslots.setslots")) {
            ItemStack target = ((Player) sender).getInventory().getItemInMainHand();
            if (target.getType().isAir()) {
                sender.sendMessage(Messages.getMessages("error-no-item"));
                return;
            }
            if (args.length == 1) {
                String itemID = CheckValidHook.checkValid(target);
                int slot = ItemLimits.getMaxEnchantments(target, ConfigReader.getDefaultLimits((Player) sender, itemID), itemID);
                ItemLimits.setMaxEnchantments(target, slot + 1);
                sender.sendMessage(Messages.getMessages("success-set")
                        .replace("%amount%", String.valueOf(slot + 1)));
                return;
            }
            ItemLimits.setMaxEnchantments(target, Integer.parseInt(args[1]));
            sender.sendMessage(Messages.getMessages("success-set").replace("%amount%", args[1]));
        } else {
            sender.sendMessage(Messages.getMessages("help-main"));
        }
    }
}
