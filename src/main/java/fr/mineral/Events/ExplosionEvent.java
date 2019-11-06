package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplosionEvent implements Listener {
    @EventHandler
    public void onExplosionEvent(ExplosionPrimeEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused() || !mineralcontest.plugin.getGame().isGameStarted())
            event.setCancelled(true);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if(mineralcontest.plugin.getGame().isGamePaused() || !mineralcontest.plugin.getGame().isGameStarted())
            e.setCancelled(true);
    }


}
