package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SubSetSlots {

    public static void SubSetSlotsCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ConfigReader.getMessages("error-in-game"));
        } else if (sender.hasPermission("enchantmentslots.setslots")) {
            ItemStack target = ((Player) sender).getInventory().getItemInMainHand();
            if (target.getType().isAir()) {
                sender.sendMessage(ConfigReader.getMessages("error-no-item"));
                return;
            }
            if (args.length == 1) {
                ItemLimits.setMaxEnchantments(target, ItemLimits.getMaxEnchantments(target) + 1);
                sender.sendMessage(ConfigReader.getMessages("success-set")
                        .replace("%amount%", String.valueOf(ItemLimits.getMaxEnchantments(target) + 1)));
                return;
            }
            ItemLimits.setMaxEnchantments(target, Integer.parseInt(args[1]));
            sender.sendMessage(ConfigReader.getMessages("success-set").replace("%amount%", args[1]));
        } else {
            sender.sendMessage(ConfigReader.getMessages("help-main"));
        }
    }
}
