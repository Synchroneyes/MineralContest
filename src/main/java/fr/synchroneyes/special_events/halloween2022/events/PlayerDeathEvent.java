package fr.synchroneyes.special_events.halloween2022.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Bukkit.broadcastMessage("Player " + event.getEntity().getDisplayName() + " est mort ! Halloween hahaha");
    }
}
