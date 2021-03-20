package fr.synchroneyes.groups.Commands.Groupe;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGroupe extends CommandTemplate {


    public JoinGroupe() {
        addArgument("Nom du groupe", true);
        constructArguments();

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(NO_GROUP);
        accessCommande.add(REQUIRE_COMMUNITY_VERSION);
        accessCommande.add(GAME_NOT_STARTED);



    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;

        for (Groupe groupe : mineralcontest.plugin.groupes)
            if (groupe.getNom().equalsIgnoreCase(args[0])) {

                // On commence par regarder si le groupe est ouvert ou fermé
                if(groupe.isGroupLocked()) {
                    // Le groupe est fermé, on regarde si le joueur a été invité ou non
                    if(groupe.isPlayerInvited(joueur)) {
                        groupe.addJoueur(joueur);
                        return true;
                    }

                    // Sinon, message d'erreur car joueur non invité
                    joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_cant_join_this_group.toString());
                    groupe.sendToadmin(mineralcontest.prefixPrive + "Le joueur " + joueur.getDisplayName() + " a tenté de rejoindre le groupe sans invitation. Invitez le avec la commande /invitergroupe <nom>");
                    return false;
                }

                // Le groupe est ouvert, on ajoute le joueur
                groupe.addJoueur(joueur);
                return true;

            }
        joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_group_doesnt_exists.toString());
        return false;
    }

    @Override
    public String getCommand() {
        return "joingroupe";
    }


    @Override
    public String getDescription() {
        return "Permet de rejoindre un groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
