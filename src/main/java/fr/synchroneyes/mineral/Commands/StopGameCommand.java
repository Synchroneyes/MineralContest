package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(player);
        if (playerGroup == null) {
            player.sendMessage(mineralcontest.prefixErreur + Lang.error_you_must_be_in_a_group.toString());
            return false;
        }

        if (!playerGroup.isAdmin(player)) {
            sender.sendMessage(mineralcontest.prefixErreur + Lang.error_you_must_be_group_admin.toString());
            return false;
        }

        if (mineralcontest.isInAMineralContestWorld(player) && !mineralcontest.plugin.pluginWorld.equals(player.getWorld())) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (command.getName().equalsIgnoreCase("stopGame")) {

                // On est jamais trop prudent ...
                if (partie.isGameStarted()) {
                    try {
                        partie.terminerPartie();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_not_started.toString()));
                }

            }
        }


        return false;
    }
}
