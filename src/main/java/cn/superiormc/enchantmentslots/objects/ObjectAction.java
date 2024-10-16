package cn.superiormc.enchantmentslots.objects;

import cn.superiormc.enchantmentslots.utils.CommonUtil;
import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class ObjectAction {

    private final List<String> actions;

    public ObjectAction(List<String> actions) {
        this.actions = actions;
    }

    public void doAction(Player player, int amount) {
        if (player == null) {
            return;
        }
        for (String singleAction : actions) {
            singleAction = replacePlaceholder(singleAction, player, amount);
            if (singleAction.startsWith("none")) {
                return;
            } else if (singleAction.startsWith("sound: ")) {
                // By: iKiwo
                String soundData = singleAction.substring(7);
                String[] soundParts = soundData.split(";;");
                if (soundParts.length >= 1) {
                    String soundName = soundParts[0];
                    float volume = 1.0f;
                    float pitch = 1.0f;
                    if (soundParts.length >= 2) {
                        try {
                            volume = Float.parseFloat(soundParts[1]);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    if (soundParts.length >= 3) {
                        try {
                            pitch = Float.parseFloat(soundParts[2]);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    Location location = player.getLocation();
                    player.playSound(location, soundName, volume, pitch);
                }
            } else if (singleAction.startsWith("message: ")) {
                player.sendMessage(TextUtil.parse(player, singleAction.substring(9)));
            } else if (singleAction.startsWith("announcement: ")) {
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                for (Player p : players) {
                    p.sendMessage(TextUtil.parse(player, singleAction.substring(14)));
                }
            } else if (singleAction.startsWith("effect: ")) {
                try {
                    if (PotionEffectType.getByName(singleAction.substring(8).split(";;")[0].toUpperCase()) == null) {
                        continue;
                    }
                    PotionEffect effect = new PotionEffect(PotionEffectType.getByName(singleAction.substring(8).split(";;")[0].toUpperCase()),
                            Integer.parseInt(singleAction.substring(8).split(";;")[2]),
                            Integer.parseInt(singleAction.substring(8).split(";;")[1]) - 1,
                            true,
                            true,
                            true);
                    player.addPotionEffect(effect);
                }
                catch (ArrayIndexOutOfBoundsException ignored) {
                }
            } else if (singleAction.startsWith("entity_spawn: ")) {
                if (singleAction.split(";;").length == 1) {
                    EntityType entity = EntityType.valueOf(singleAction.substring(14).split(";;")[0].toUpperCase());
                    player.getLocation().getWorld().spawnEntity(player.getLocation(), entity);
                } else if (singleAction.split(";;").length == 5) {
                    World world = Bukkit.getWorld(singleAction.substring(14).split(";;")[1]);
                    Location location = new Location(world,
                            Double.parseDouble(singleAction.substring(14).split(";;")[2]),
                            Double.parseDouble(singleAction.substring(14).split(";;")[3]),
                            Double.parseDouble(singleAction.substring(14).split(";;")[4]));
                    EntityType entity = EntityType.valueOf(singleAction.substring(14).split(";;")[0].toUpperCase());
                    if (location.getWorld() == null) {
                        continue;
                    }
                    location.getWorld().spawnEntity(location, entity);
                }
            } else if (singleAction.startsWith("teleport: ")) {
                try {
                    if (singleAction.split(";;").length == 4) {
                        Location loc = new Location(Bukkit.getWorld(singleAction.substring(10).split(";;")[0]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[1]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[2]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[3]),
                                player.getLocation().getYaw(),
                                player.getLocation().getPitch());
                        player.teleport(loc);
                    }
                    else if (singleAction.split(";;").length == 6) {
                        Location loc = new Location(Bukkit.getWorld(singleAction.split(";;")[0]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[1]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[2]),
                                Double.parseDouble(singleAction.substring(10).split(";;")[3]),
                                Float.parseFloat(singleAction.substring(10).split(";;")[4]),
                                Float.parseFloat(singleAction.substring(10).split(";;")[5]));
                        player.teleport(loc);
                    }
                }
                catch (ArrayIndexOutOfBoundsException ignored) {
                }
            } else if (singleAction.startsWith("console_command: ")) {
                CommonUtil.dispatchCommand(singleAction.substring(17));
            } else if (singleAction.startsWith("player_command: ")) {
                CommonUtil.dispatchCommand(player, singleAction.substring(16));
            } else if (singleAction.startsWith("op_command: ")) {
                CommonUtil.dispatchOpCommand(player, singleAction.substring(12));
            } else if (singleAction.equals("close")) {
                player.closeInventory();
            }
        }
    }
    private static String replacePlaceholder(String str, Player player, int amount){
        return str.replace("{world}", player.getWorld().getName())
                .replace("{amount}", String.valueOf(amount))
                .replace("{player_x}", String.valueOf(player.getLocation().getX()))
                .replace("{player_y}", String.valueOf(player.getLocation().getY()))
                .replace("{player_z}", String.valueOf(player.getLocation().getZ()))
                .replace("{player_pitch}", String.valueOf(player.getLocation().getPitch()))
                .replace("{player_yaw}", String.valueOf(player.getLocation().getYaw()))
                .replace("{player}", player.getName());
    }
}
