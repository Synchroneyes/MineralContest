package fr.groups.Commands.Groupe;

import fr.groups.Commands.CommandTemplate;
import fr.groups.Core.Groupe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CreerGroupe extends CommandTemplate {


    public CreerGroupe() {
        super();
        addArgument("nom", true);
        constructArguments();

        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(REQUIRE_COMMUNITY_VERSION);
        accessCommande.add(NO_GROUP);

    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        if (!joueur.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_hub_world.toString());
            return false;
        }


        Groupe nouveauGroupe = new Groupe();
        nouveauGroupe.setNom(args[0]);
        nouveauGroupe.addJoueur(joueur);
        nouveauGroupe.addAdmin(joueur);
        mineralcontest.plugin.creerNouveauGroupe(nouveauGroupe);

        return false;
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

}