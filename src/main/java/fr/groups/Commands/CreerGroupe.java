package fr.groups.Commands;

import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CreerGroupe extends CommandTemplate {


    public CreerGroupe() {
        super();
        this.arguments.add("nom");
        constructArguments();

    }

    @Override
    public String getCommand() {
        return "creergroupe";
    }

    @Override
    public String getDescription() {
        return "Permet de créer un groupe";
    }

    @Override
    public String getPermissionRequise() {
        return "";
    }


    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        if (commandSender instanceof Player) {
            Player joueur = (Player) commandSender;
            if (!joueur.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
                commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_hub_world.toString());
                return false;
            }


            if (mineralcontest.getPlayerGroupe(joueur) == null) {
                if (command.equalsIgnoreCase(getCommand()) && args.length == this.arguments.size()) {
                    Groupe nouveauGroupe = new Groupe();
                    nouveauGroupe.setNom(args[0]);
                    nouveauGroupe.addJoueur(joueur);
                    nouveauGroupe.addAdmin(joueur);
                    mineralcontest.plugin.creerNouveauGroupe(nouveauGroupe);
                } else {
                    joueur.sendMessage(mineralcontest.prefixErreur + getUsage());
                    return false;
                }
            } else {
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.error_you_already_have_a_group.toString());
                return false;
            }
        } else {
            commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }
        return false;
    }
}