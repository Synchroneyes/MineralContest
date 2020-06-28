package fr.synchroneyes.mineral.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class BowEvent implements Listener {

    @EventHandler
    public void onBowHit(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getProjectile() instanceof Arrow) {
                Arrow fleche = (Arrow) event.getProjectile();
                event.getEntity().sendMessage("dégats: " + fleche.getDamage() + " - hit: " + fleche.getFallDistance());


            }
        }
    }

    @EventHandler
    public void test(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow fleche = (Arrow) event.getEntity();
            Bukkit.broadcastMessage("HIT: " + fleche.getDamage() + " - LOC: " + fleche.getLocation().getBlock().getLocation().toVector().toString());
        }
    }
}
