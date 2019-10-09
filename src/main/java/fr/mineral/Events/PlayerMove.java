package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        // Si la partie est en pause, on annule le mouvement
        if(mineralcontest.isGamePaused()) {
            event.setCancelled(true);
        }
    }
}
