package fr.synchroneyes.mineral.Shop.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class SpawnShop extends CommandTemplate {

    public SpawnShop() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(PLAYER_ADMIN);
    }


    @Override
    public String getCommand() {
        return "spawnshop";
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
        Location locationBot = joueur.getLocation();

        Villager seller = joueur.getWorld().spawn(locationBot, Villager.class);
        seller.setAI(false);
        seller.setCustomName("Boutique");
        seller.setCustomNameVisible(true);
        seller.setProfession(Villager.Profession.NITWIT);


        return false;
    }
}
