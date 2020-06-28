package fr.synchroneyes.groups.Commands.Groupe;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviterGroupe extends CommandTemplate {

    public InviterGroupe() {
        super();
        addArgument("nom du joueur", true);
        constructArguments();

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(GROUP_REQUIRED);
        this.accessCommande.add(GROUP_ADMIN);
        this.accessCommande.add(REQUIRE_GROUP_UNLOCKED);
        accessCommande.add(REQUIRE_COMMUNITY_VERSION);
        accessCommande.add(GAME_NOT_STARTED);


    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

        Player joueurInvite = Bukkit.getPlayer(args[0]);
        if (joueurInvite == null) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
            return false;
        }

        playerGroup.inviterJoueur(joueurInvite);
        return false;
    }

    @Override
    public String getCommand() {
        return "invitergroupe";
    }


    @Override
    public String getDescription() {
        return "Inviter un joueur dans le groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
