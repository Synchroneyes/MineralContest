package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteract implements Listener {

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {

        if (MapBuilder.getInstance().isBuilderModeEnabled) return;

        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (event.getEntity() instanceof Player) {
                if (partie != null && partie.isGamePaused())
                    event.setCancelled(true);
            }
        }


    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {

        if (MapBuilder.getInstance().isBuilderModeEnabled) return;

        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (event.getEntity() instanceof Player)
                if (partie != null && partie.isGamePaused())
                    if (event.getDamager() instanceof Monster || event.getDamager() instanceof Arrow)
                        event.setCancelled(true);
        }

    }
}
