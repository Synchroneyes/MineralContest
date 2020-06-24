package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SwitchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player) && !mineralcontest.plugin.pluginWorld.equals(player.getWorld())) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (command.getName().equalsIgnoreCase("switch")) {
                if (sender.isOp()) {
                    if (args.length == 2) {
                        try {
                            partie.switchPlayer(Bukkit.getPlayer(args[0]), args[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage("Usage: /switch <player> <team>");
                        return true;
                    }
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + "access denied");
                }
            }
        }


        return false;
    }
}
