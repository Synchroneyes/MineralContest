package fr.groups.Commands.Admin;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AjouterAdmin extends CommandTemplate {

    public AjouterAdmin() {
        super();

        addArgument("Nom du joueur", true);

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(GROUP_REQUIRED);
        this.accessCommande.add(GROUP_CREATOR);

        constructArguments();

    }

    @Override
    public String getCommand() {
        return "ajouteradmin";
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        if (commandSender instanceof Player) {
            Player joueur = (Player) commandSender;

            try {
                canPlayerUseCommand(joueur, args);
            } catch (Exception e) {
                joueur.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                return false;
            }

            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            Player joueurAMettreAdmin = Bukkit.getPlayer(args[0]);
            if (joueurAMettreAdmin == null) {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
                return false;
            }

            if (mineralcontest.getPlayerGroupe(joueurAMettreAdmin) == null || !playerGroup.containsPlayer(joueurAMettreAdmin)) {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_player_not_in_our_group.toString());
                return false;
            }

            playerGroup.addAdmin(joueurAMettreAdmin);


        } else {
            commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Mets un joueur du groupe en admin";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
