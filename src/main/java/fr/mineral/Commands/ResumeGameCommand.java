package fr.mineral.Commands;

import fr.mineral.Core.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResumeGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("resume")) {
            Equipe teamNonPleine = mineralcontest.plugin.getGame().getEquipeNonPleine();
            if(mineralcontest.plugin.getGame().isGamePaused() && teamNonPleine == null) {
                mineralcontest.plugin.getGame().resumeGame();
            } else {
                sender.sendMessage(mineralcontest.prefixErreur + "Cette commande n'est pas disponible: La partie n'est pas en pause ou une équipe n'est pas pleine");
                if(teamNonPleine != null) sender.sendMessage(mineralcontest.prefixErreur + "L'équipe " + teamNonPleine.getCouleur() + teamNonPleine.getNomEquipe() + ChatColor.WHITE + " n'est pas pleine.");
            }
        }
        return false;
    }
}
