package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GetPlayerDistance extends CommandTemplate {

    public GetPlayerDistance() {
        addArgument("nom Joueur", true);
    }

    @Override
    public String getCommand() {
        return "GetPlayerDistance";
    }

    @Override
    public String getDescription() {
        return "null";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {

        Player targetedPlayer = Bukkit.getPlayer(args[0]);

        Player joueur = (Player) commandSender;

        if(targetedPlayer == null) {
            joueur.sendMessage("Le joueur n'existe pas");
            return false;
        }

        // On récupère la distance du joueur
        Location targetedLocation = targetedPlayer.getLocation();

        // On calcule la distance
        int x1, x2, z1, z2;
        x1 = joueur.getLocation().getBlockX();
        z1 = joueur.getLocation().getBlockZ();

        x2 = targetedLocation.getBlockX();
        z2 = targetedLocation.getBlockZ();

        double distance;
        distance = Math.pow((x1-x2), 2) + Math.pow((z1-z2), 2);
        distance = Math.sqrt(distance);


        targetedLocation.setY(targetedLocation.getBlockY()+2);

        Location directionVersDestination = targetedLocation.subtract(joueur.getEyeLocation());

        Vector playerDirection = joueur.getEyeLocation().getDirection();

        double angle = directionVersDestination.getPitch();

        angle = Math.toDegrees(angle);


        String movedirection = "";
            if(angle < -135 || angle >= 135){
            movedirection = "Front";
            };
            if(angle < 135 && angle >= 45){
            movedirection = "right";
            };
            if(angle < 45 && angle >= -45){
            movedirection = "back";
            };
            if(angle < -45 && angle >= -135){
            movedirection = "left";
            };



        //double alpha = Math.asin(distanceLoc.getX() / distanceLoc.getZ());


        joueur.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(targetedPlayer.getDisplayName() + " " + Math.round(distance)));

        return false;
    }

    public static String getDirection(final Location location) {
        double rotation = (location.getYaw() - 90.0f) % 360.0f;

        //double rotation = Math.asin(location.getX() / location.getZ());

        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return "NORTH";
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return "NORTHEAST";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "EAST";
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return "SOUTHEAST";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "SOUTH";
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return "SOUTHWEST";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "WEST";
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return "NORTHWEST";
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return "NORTH";
        }
        return "NORTH";
    }
}
