package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_reset_team_penality implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (sender.isOp() && command.getName().equals("mp_reset_team_penality")) {
                if (args.length == 1) {
                    String usage = "Usage: /mp_reset_team_penality <" + Lang.red_team.toString() + " | " + Lang.yellow_team.toString() + " | " + Lang.blue_team.toString() + ">";

                    House maison = partie.getHouseFromName(args[0]);
                    if (maison == null) {
                        player.sendMessage(mineralcontest.prefixErreur + Lang.cvar_error_invalid_team_name.toString());
                        return false;
                    }

                    maison.getTeam().resetPenalty();

                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
