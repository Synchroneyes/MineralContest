package fr.groups.Commands.Groupe;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickPlayerFromGroup extends CommandTemplate {


    public KickPlayerFromGroup() {
        super();
        this.arguments.add("Nom du joueur");
        constructArguments();

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(GROUP_REQUIRED);
        this.accessCommande.add(GROUP_ADMIN);

    }

    @Override
    public String getCommand() {
        return "kickplayer";
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
            Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);


            Player joueurAKick = Bukkit.getPlayer(args[0]);
            if (joueurAKick == null) {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
                return false;
            }

            if (joueurAKick.equals(joueur)) {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_cant_kick_yourself_from_group.toString());
                return false;
            }

            if (!playerGroupe.containsPlayer(joueurAKick)) {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_no_player_with_this_name.toString());
                return false;
            }

            if (playerGroupe.isAdmin(joueurAKick)) {
                // On veut seulement kick un admin si ça vient du créateur du groupe
                if (playerGroupe.isGroupeCreateur(joueur)) {
                    // On fait le kick
                    playerGroupe.kickPlayer(joueurAKick);
                    return false;
                }
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_cant_kick_this_player_from_the_group.toString());
                return false;
            }


            // Si le joueur qui fait la commande est admin
            if (playerGroupe.isAdmin(joueur)) {
                playerGroupe.kickPlayer(joueurAKick);
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
        return "Exclure un joueur du groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
