package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplosionEvent implements Listener {
    @EventHandler
    public void onExplosionEvent(ExplosionPrimeEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (partie != null && (partie.isGamePaused() || !partie.isGameStarted()))
                event.setCancelled(true);
        }

    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        World worldEvent = e.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (partie != null && (partie.isGamePaused() || !partie.isGameStarted()))
                e.setCancelled(true);
        }

    }


}
