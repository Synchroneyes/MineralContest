package fr.mineral.Commands;

import fr.groups.Utils.Etats;
import fr.mineral.Core.Game.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*Player player = (Player) commandSender;
        if(mineralcontest.isInAMineralContestWorld(player)) {
            Game game = mineralcontest.getPlayerGame(player);
            Player joueur = (Player) commandSender;

            // TODO
            if(game.isGamePaused()) {
                if(!game.isPlayerReady(joueur)) {
                    try {
                        game.setPlayerReady(joueur);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else game.removePlayerReady(joueur);
            } else {
                joueur.sendMessage("vote is not ended");
            }
        }*/


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
            if (command.getName().equalsIgnoreCase("ready")) {

                if (partie.groupe.getEtatPartie().equals(Etats.ATTENTE_DEBUT_PARTIE)) {
                    try {
                        if (!partie.isPlayerReady(player)) partie.setPlayerReady(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Error.Report(e, partie);
                    }
                }
            }
        }
        return false;
    }
}
