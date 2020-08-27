package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.Utils.ArmorStandUtility;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorStandCommand extends CommandTemplate {

    public ArmorStandCommand() {
        accessCommande.add(PLAYER_COMMAND);
    }

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

        Location armorLocation = joueur.getLocation();
        ArmorStandUtility.createArmorStandWithColoredLeather(armorLocation, ChatColor.RED + "Equipe Rouge", Color.RED, Material.GOLDEN_SWORD);

        Location secondLocation = armorLocation.clone();
        secondLocation.setX(secondLocation.getBlockX()+2);
        ArmorStandUtility.createArmorStandWithColoredLeather(secondLocation, ChatColor.GREEN + "Equipe Verte", Color.GREEN, Material.DIAMOND_SWORD);

        Location thirdLocation = armorLocation.clone();
        thirdLocation.setX(thirdLocation.getBlockX()-2);
        ArmorStandUtility.createArmorStandWithColoredLeather(thirdLocation, ChatColor.BLUE + "Equipe Bleu", Color.BLUE, Material.WOODEN_SWORD);

        return false;
    }
}
