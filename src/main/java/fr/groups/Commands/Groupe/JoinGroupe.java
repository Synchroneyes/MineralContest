package fr.groups.Commands.Groupe;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Commands.JoinCommand;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGroupe extends CommandTemplate {


    public JoinGroupe() {
        this.arguments.add("Nom du groupe");
        constructArguments();

        this.accessCommande.add(PLAYER_COMMAND);
        this.accessCommande.add(NO_GROUP);
    }

    @Override
    public String getCommand() {
        return "joingroupe";
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        if (commandSender instanceof Player) {
            if (command.equalsIgnoreCase(getCommand())) {
                Player joueur = (Player) commandSender;
                Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

                try {
                    canPlayerUseCommand(joueur, args);
                } catch (Exception e) {
                    joueur.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                    return false;
                }

                for (Groupe groupe : mineralcontest.plugin.groupes)
                    if (groupe.getNom().equalsIgnoreCase(args[0])) {
                        if (groupe.isPlayerInvited(joueur)) {
                            groupe.addJoueur(joueur);
                        } else {
                            joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_cant_join_this_group.toString());
                            groupe.sendToadmin(mineralcontest.prefixPrive + "Le joueur " + joueur.getDisplayName() + " a tenté de rejoindre le groupe sans invitation. Invitez le avec la commande /invitergroupe <nom>");
                        }
                        return false;
                    }
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_group_doesnt_exists.toString());
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
        return "Permet de rejoindre un groupe";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
