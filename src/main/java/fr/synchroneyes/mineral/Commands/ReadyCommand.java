package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReadyCommand implements CommandExecutor {
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
            if (command.getName().equalsIgnoreCase("ready")) {

                if (partie.groupe.getEtatPartie().equals(Etats.EN_ATTENTE)) {
                    try {
                        partie.setPlayerReady(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (partie.groupe.getEtatPartie().equals(Etats.ATTENTE_DEBUT_PARTIE)) {
                    try {
                        if (!partie.isPlayerReady(player)) partie.setPlayerReady(player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
