package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.mineral.Core.Coffre.Coffres.CoffreMortJoueur;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Kits.Classes.Mineur;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerKilledByPlayer implements Listener {

    /**
     * Méthode permettant de faire aparaitre un coffre à la mort d'un joueur avec son inventaire
     * @param event
     */
    @EventHandler
    public void onPlayerKilled(PlayerDeathByPlayerEvent event) {
        Player deadPlayer = event.getPlayerDead();

        // Si l'apparition de coffre est désactivée, on ne fait rien
        MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(deadPlayer);
        if(mcPlayer == null) return;

        if(mcPlayer.getGroupe().getParametresPartie().getCVAR("drop_chest_on_death").getValeurNumerique() != 1) return;


        // On récupère la position du joueur
        Location deadLocation = new Location(deadPlayer.getWorld(), deadPlayer.getLocation().getBlockX(), deadPlayer.getLocation().getBlockY(), deadPlayer.getLocation().getBlockZ());
        while(deadLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            int y = deadLocation.getBlockY();
            y--;
            deadLocation.setY(y);
        }

        Location arenaCenterLocation = event.getPartie().getArene().getCoffre().getLocation();

        int distanceProtectedArenaRadius = event.getPartie().groupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();


        // Si le joueur est autour de l'arène, on ne fait rien
        if(Radius.isBlockInRadius(deadLocation, arenaCenterLocation, distanceProtectedArenaRadius)) return;


        // On ajoute un nouveau coffre de mort
        CoffreMortJoueur coffreMortJoueur = new CoffreMortJoueur(5*9, mcPlayer.getGroupe().getAutomatedChestManager(), deadPlayer);
        // On met sa position
        coffreMortJoueur.setChestLocation(deadLocation);

        mcPlayer.getGroupe().getAutomatedChestManager().addTimedChest(coffreMortJoueur);

        coffreMortJoueur.spawn();

        deadPlayer.getInventory().clear();






    }
}
