package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_add_team_penality implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }
        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie != null) {
                if (sender.isOp() && command.getName().equals("mp_add_team_penality")) {
                    String usage = "Usage: /mp_add_team_penality <" + Lang.red_team.toString() + " | " + Lang.yellow_team.toString() + " | " + Lang.blue_team.toString() + ">  <amount> ";
                    if (args.length == 2) {

                        int valeur = 0;
                        try {
                            valeur = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            sender.sendMessage(usage);
                            return true;
                        }

                        House maison = partie.getHouseFromName(args[0]);
                        if (maison == null) {
                            player.sendMessage(mineralcontest.prefixErreur + Lang.cvar_error_invalid_team_name.toString());
                            return false;
                        }

                        maison.getTeam().addPenalty(valeur);
                    }
                    return false;
                }
            }

        }

        return true;
    }
}
