package fr.synchroneyes.groups.Commands.Groupe;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OuvrirGroupe extends CommandTemplate {

    public OuvrirGroupe(){
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(REQUIRE_COMMUNITY_VERSION);
        accessCommande.add(GROUP_REQUIRED);
        accessCommande.add(GAME_NOT_STARTED);
        accessCommande.add(GROUP_ADMIN);
    }

    @Override
    public String getCommand() {
        return "mc_ouvrirgroupe";
    }

    @Override
    public String getDescription() {
        return "Commande permettant de ouvrir le groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {

        // On sait que le joueur est déjà dans un group, et qu'il est admin
        Player joueur = (Player) commandSender;

        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

        // ON récupère son groupe
        Groupe groupe = mcPlayer.getGroupe();

        if(!groupe.isGroupLocked()) {
            joueur.sendMessage(mineralcontest.prefixErreur + "Le groupe est déjà ouvert!");
            return true;
        }

        groupe.setGroupLocked(false);

        joueur.sendMessage(mineralcontest.prefixPrive + "Le groupe est désormais ouvert.");



        return false;
    }
}
