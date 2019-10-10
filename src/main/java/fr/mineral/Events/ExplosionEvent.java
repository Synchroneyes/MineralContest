package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplosionEvent implements Listener {
    @EventHandler
    public void onExplosionEvent(ExplosionPrimeEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);
    }
}
