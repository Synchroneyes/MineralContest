package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.PlayerPermissionChangeEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPermissionChange implements Listener {

    /**
     * Méthode appelée lors du changement de status OP d'un joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerPermissionChange(PlayerPermissionChangeEvent event) {
        Player player = event.getPlayer();
        /*
            Si le joueur est dans un monde mineral contest, on a des changements à faire
            Si le joueur est désormais OP, on le passe GroupAdmin.
         */
        if (mineralcontest.isInAMineralContestWorld(player)) {

            Groupe playerGroupe = mineralcontest.getPlayerGroupe(player);
            if (playerGroupe == null) return;


            // Si le joueur est désormais OP, on l'ajoute en tant qu'admin de son groupe !
            if (player.isOp()) {
                if (!playerGroupe.isAdmin(player)) playerGroupe.addAdmin(player);
            }
        }
    }

}
