package cn.superiormc.enchantmentslots.commands;

import cn.superiormc.enchantmentslots.gui.EnchantGUI;
import cn.superiormc.enchantmentslots.managers.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SubOpenEnchantGUI extends AbstractCommand {

    public SubOpenEnchantGUI() {
        this.id = "openenchantgui";
        this.requiredPermission =  "enchantmentslots." + id;
        this.onlyInGame = false;
        this.requiredArgLength = new Integer[]{1, 2};
    }

    @Override
    public void executeCommandInGame(String[] args, Player player) {
        if (args.length > 2) {
            LanguageManager.languageManager.sendStringText(player, "error-args");
            return;
        }
        EnchantGUI gui = new EnchantGUI(player);
        gui.openGUI();
    }

    @Override
    public void executeCommandInConsole(String[] args) {
        if (args.length == 1) {
            LanguageManager.languageManager.sendStringText("error-args");
            return;
        }
        Player whoWillAdd = Bukkit.getPlayer(args[1]);
        if (whoWillAdd == null) {
            LanguageManager.languageManager.sendStringText("error-player-not-found", "player", args[1]);
            return;
        }
        EnchantGUI gui = new EnchantGUI(whoWillAdd);
        gui.openGUI();
    }
}
