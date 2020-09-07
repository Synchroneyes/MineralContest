package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.Utils.ArmorStandUtility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArmorStand extends CommandTemplate {
    @Override
    public String getCommand() {
        return "armorstand";
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


        Player joueur = (Player) commandSender;
        Location spawnLocation = joueur.getLocation();

        ArmorStandUtility.createArmorStandWithColoredLeather(spawnLocation, "Test", Color.RED, Material.GOLDEN_SWORD);
        joueur.sendMessage("spawned!");

        return false;
    }
}
