package fr.mineral.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        // TODO
        // Si la partie est en pause, on annule le mouvement
        /*if(mineralcontest.isGamePaused()) {
            event.setCancelled(true);
       */
    }
}
