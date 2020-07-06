package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        Bukkit.getLogger().info("Creeper explode!");
        World worldEvent = e.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (partie == null) return;
            // On récupère le centre de l'arène
            Location centreArene = partie.getArene().getCoffre().getLocation();
            int rayonProtection = partie.groupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();

            if (partie != null && (partie.isGamePaused() || !partie.isGameStarted()) || Radius.isBlockInRadius(centreArene, e.getLocation(), rayonProtection))
                e.setCancelled(true);
        }

    }


}
