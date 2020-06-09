package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) commandSender;


        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (command.getName().equalsIgnoreCase("leaveteam")) {
                if (!partie.isGameStarted() && !partie.isPreGame() && !partie.isGamePaused()) {
                    Equipe playerTeam = partie.getPlayerTeam(player);
                    if (playerTeam != null) playerTeam.removePlayer(player);
                    return false;
                }
                return false;
            }
        }

        return false;
    }

}
