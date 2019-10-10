package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {


        if(mineralcontest.plugin.getGame().isGamePaused()) {
            Location to = event.getFrom();
            to.setPitch(event.getTo().getPitch());
            to.setYaw(event.getTo().getYaw());
            event.setTo(to);
        }

    }
}
