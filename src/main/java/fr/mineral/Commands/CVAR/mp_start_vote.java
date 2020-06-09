package fr.mineral.Commands.CVAR;

import fr.groups.Core.Groupe;
import fr.groups.Utils.Etats;
import fr.mineral.Core.Game.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_start_vote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }
            Groupe playerGroup = partie.groupe;

            if (playerGroup.getEtatPartie().equals(Etats.VOTE_EN_COURS)) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.vote_already_started.toString());
                return false;
            }

            if (!playerGroup.getEtatPartie().equals(Etats.EN_ATTENTE)) {
                sender.sendMessage(Lang.error_vote_available_only_when_no_game.toString());
                return false;
            }


            if(command.getName().equals("mp_start_vote")) {
                if (playerGroup.isAdmin(player)) {
                    playerGroup.setEtat(Etats.VOTE_EN_COURS);
                    playerGroup.initVoteMap();
                    playerGroup.setGroupLocked(true);
                    playerGroup.sendToEveryone(mineralcontest.prefixGroupe + Lang.vote_started.toString());
                    return false;
                }
            }
        }

        return false;
    }
}
