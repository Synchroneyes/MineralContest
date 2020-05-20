package fr.mapbuilder.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mapbuilder.Core.Monde;
import fr.mapbuilder.MapBuilder;
import fr.mineral.mineralcontest;
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
    public String getCommand() {
        return "mcarena";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        try {
            canPlayerUseCommand(commandSender, args);
        } catch (Exception e) {
            commandSender.sendMessage(mineralcontest.prefixErreur + getUsage());
            return false;
        }

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
