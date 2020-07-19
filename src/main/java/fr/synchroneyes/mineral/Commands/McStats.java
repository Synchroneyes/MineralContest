package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class McStats extends CommandTemplate {

    public McStats() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GAME_ENDED);
    }

    @Override
    public String getCommand() {
        return "mcstats";
    }

    @Override
    public String getDescription() {
        return "permet d'ouvrir le menu de stats";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;

        Game partie = mineralcontest.getPlayerGame(joueur);

        joueur.openInventory(partie.getMenuStatistiques());
        return false;
    }
}
