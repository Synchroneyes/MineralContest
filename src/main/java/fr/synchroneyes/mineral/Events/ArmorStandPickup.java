package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 * Classe permettant d'empêcher les joueurs de prendre les items sur les armorstand
 */
public class ArmorStandPickup implements Listener {

    @EventHandler
    public void onPlayerItemPicked(PlayerInteractAtEntityEvent event) {
        Player joueur = event.getPlayer();

        if(mineralcontest.isInAMineralContestWorld(joueur)) {
            // On vérifie que c'est une interaction sur un armorstand
            if(event.getRightClicked() instanceof ArmorStand) {
                event.setCancelled(true);
            }
        }
    }
}
