package fr.groups.Commands.Admin;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RetirerAdmin extends CommandTemplate {

    public RetirerAdmin() {
        super();
        addArgument("Nom du joueur", true);
        constructArguments();

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(GROUP_REQUIRED);
        this.accessCommande.add(GROUP_CREATOR);
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

        Player adminARetirer = Bukkit.getPlayer(args[0]);
        if (adminARetirer == null) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
            return false;
        }

        Groupe otherPlayerGroup = mineralcontest.getPlayerGroupe(adminARetirer);

        if (otherPlayerGroup == null || !otherPlayerGroup.equals(playerGroup)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_player_not_in_our_group.toString());
            return false;
        }

        if (!playerGroup.isAdmin(adminARetirer)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.error_player_is_not_admin.toString(), adminARetirer));
            return false;
        }

        if (playerGroup.isGroupeCreateur(adminARetirer)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_cant_remove_this_admin.toString());
            return false;
        }

        playerGroup.removeAdmin(adminARetirer);

        return false;
    }

    @Override
    public String getCommand() {
        return "retireradmin";
    }


    @Override
    public String getDescription() {
        return "Retire un administrateur du groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
