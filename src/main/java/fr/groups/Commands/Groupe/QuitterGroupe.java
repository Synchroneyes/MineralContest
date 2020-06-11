package fr.groups.Commands.Groupe;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuitterGroupe extends CommandTemplate {

    public QuitterGroupe() {
        super();
        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(this.GROUP_REQUIRED);
        accessCommande.add(REQUIRE_COMMUNITY_VERSION);

    }

    @Override
    public String getCommand() {
        return "quittergroupe";
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
            playerGroup.retirerJoueur(joueur);
            return false;

        } else {
            commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "Permet de quitter son groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
