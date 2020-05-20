package fr.groups.Commands.Groupe;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
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
    }

    @Override
    public String getCommand() {
        return "invitergroupe";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (s.equalsIgnoreCase(getCommand())) {
                Player joueur = (Player) commandSender;
                Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

                try {
                    canPlayerUseCommand(joueur, args);
                } catch (Exception e) {
                    joueur.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                    return false;
                }


                Player joueurInvite = Bukkit.getPlayer(args[0]);
                if (joueurInvite == null) {
                    joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
                    return false;
                }

                playerGroup.inviterJoueur(joueurInvite);
                return false;
            }
        } else {
            commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }
        return false;
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
