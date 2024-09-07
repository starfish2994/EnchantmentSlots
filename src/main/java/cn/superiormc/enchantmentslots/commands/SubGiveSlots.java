package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.EnchantmentSlots;
import cn.superiormc.enchantmentslots.configs.ConfigReader;
import cn.superiormc.enchantmentslots.configs.Messages;
import cn.superiormc.enchantmentslots.hooks.CheckValidHook;
import cn.superiormc.enchantmentslots.methods.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.methods.ItemLimits;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SubGiveSlots {

    public static void SubGiveSlotsCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("enchantmentslots.giveslots")) {
            // giveslots <ID> <玩家名称>
            if (args.length >= 3) {
                ItemStack item = ExtraSlotsItem.getExtraSlotItem(args[1]);
                if (item == null) {
                    sender.sendMessage(Messages.getMessages("error-item-not-found"));
                    return;
                }
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(Messages.getMessages("error-player-not-found").replace(
                            "%player%", args[2]
                    ));
                    return;
                }
                int amount = 1;
                if (args.length == 4) {
                    amount = Integer.parseInt(args[3]);
                }
                item.setAmount(amount);
                player.getInventory().addItem(item);
                sender.sendMessage(Messages.getMessages("give-extra-slot-item").
                        replace("%player%", player.getName()).
                        replace("%item%", args[1]).
                        replace("%amount%", String.valueOf(amount)));
                return;
            }
            if (!(sender instanceof Player)){
                sender.sendMessage(Messages.getMessages("error-in-game"));
                return;
            }
            ItemStack target = ((Player) sender).getInventory().getItemInMainHand();
            if (target.getType().isAir()) {
                sender.sendMessage(Messages.getMessages("error-no-item"));
                return;
            }
            String itemID = CheckValidHook.checkValid(target);
            int slot = ItemLimits.getMaxEnchantments(target, ConfigReader.getDefaultLimits((Player) sender, itemID), itemID);
            if (args.length == 1) {
                ItemLimits.setMaxEnchantments(target, slot + 1);
                sender.sendMessage(Messages.getMessages("success-set")
                        .replace("%amount%", String.valueOf(slot+ 1)));
                return;
            }
            ItemLimits.setMaxEnchantments(target, ItemLimits.getMaxEnchantments(target, slot + Integer.parseInt(args[1]), itemID));
            sender.sendMessage(Messages.getMessages("success-set").replace("%amount%", args[1]));
        } else {
            sender.sendMessage(Messages.getMessages("help-main"));
        }
    }
}
