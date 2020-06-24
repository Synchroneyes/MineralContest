package fr.synchroneyes.mapbuilder.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mapbuilder.Core.Monde;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class mcarena extends CommandTemplate {

    private LinkedList<String> actionsPossible;
    public static Monde monde = MapBuilder.monde;


    public mcarena() {

        this.actionsPossible = new LinkedList<>();
        actionsPossible.add("setCoffreLocation");
        actionsPossible.add("setTeleportLocation");


        addArgument("action", true);


        if (monde == null) monde = new Monde();

        accessCommande.add(PLAYER_COMMAND);
        constructArguments();
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;
        if (args[0].equalsIgnoreCase("setCoffreLocation")) {
            Location coffreLocation = joueur.getLocation().getBlock().getLocation();
            monde.getArene().setCoffre(coffreLocation);
            joueur.sendMessage(mineralcontest.prefixPrive + "La position du coffre a bien été ajouté en " + coffreLocation.toVector().toString());
            return false;
        }

        if (args[0].equalsIgnoreCase("setTeleportLocation")) {
            Location coffreLocation = joueur.getLocation().getBlock().getLocation();
            monde.getArene().setTeleportSpawn(coffreLocation);
            joueur.sendMessage(mineralcontest.prefixPrive + "La position de téléportation de /arene a bien été ajoutée en " + coffreLocation.toVector().toString());

            return false;
        }

        return false;
    }

    @Override
    public String getCommand() {
        return "mcarena";
    }


    @Override
    public String getDescription() {
        return "Commandes relative à la création d'une arène";
    }

    @Override
    public String getPermissionRequise() {
        return "admin";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (alias.equalsIgnoreCase("mcarena")) {
            if (args.length == 0 || args.length == 1) return actionsPossible;
        }
        return new LinkedList<>();
    }
}
