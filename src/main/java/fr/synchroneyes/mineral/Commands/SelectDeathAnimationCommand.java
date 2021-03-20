package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectDeathAnimationCommand extends CommandTemplate {

    public SelectDeathAnimationCommand() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(GROUP_REQUIRED);
    }

    @Override
    public String getCommand() {
        return "mc_deathanimation";
    }

    @Override
    public String getDescription() {
        return "Permet de sélectionner une animation de mort";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        // On sait que le joueur fait partie du plugin
        Player joueur = (Player) commandSender;
        mineralcontest.plugin.deathAnimationManager.openMenuSelection(joueur);
        return false;
    }
}
