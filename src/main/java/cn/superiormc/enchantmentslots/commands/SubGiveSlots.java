package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.utils.ConfigReader;
import cn.superiormc.enchantmentslots.utils.ExtraSlotsItem;
import cn.superiormc.enchantmentslots.utils.ItemLimits;
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
                    sender.sendMessage(ConfigReader.getMessages("error-no-item"));
                    return;
                }
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(ConfigReader.getMessages("error-player-not-found").replace(
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
            }
            if (!(sender instanceof Player)){
                sender.sendMessage(ConfigReader.getMessages("error-in-game"));
                return;
            }
            ItemStack target = ((Player) sender).getInventory().getItemInMainHand();
            if (target.getType().isAir()) {
                sender.sendMessage(ConfigReader.getMessages("error-no-item"));
                return;
            }
            if (args.length == 1) {
                ItemLimits.setMaxEnchantments(target, ItemLimits.getMaxEnchantments(((Player) sender), target) + 1);
                sender.sendMessage(ConfigReader.getMessages("success-set")
                        .replace("%amount%", String.valueOf(ItemLimits.getMaxEnchantments(((Player) sender), target) + 1)));
                return;
            }
            ItemLimits.setMaxEnchantments(target, ItemLimits.getMaxEnchantments(((Player) sender), target) + Integer.parseInt(args[1]));
            sender.sendMessage(ConfigReader.getMessages("success-set").replace("%amount%", args[1]));
        } else {
            sender.sendMessage(ConfigReader.getMessages("help-main"));
        }
    }
}
