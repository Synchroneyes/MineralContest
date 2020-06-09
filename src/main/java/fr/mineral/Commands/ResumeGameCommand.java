package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResumeGameCommand implements CommandExecutor {
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

            if(command.getName().equalsIgnoreCase("resume")) {
                Equipe teamNonPleine = partie.getEquipeNonPleine(); //mineralcontest.getPlayerGame(joueur).getEquipeNonPleine();
                if (partie.isGamePaused() && teamNonPleine == null) {
                    partie.resumeGame();
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.error_when_resume.toString()));
                    if(teamNonPleine != null) sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.admin_team_non_empty.toString(), teamNonPleine));
                }
            }
        }

        return false;
    }
}
