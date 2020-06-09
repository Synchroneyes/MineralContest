package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) throws Exception {
        World worldEvent = e.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            if(e.getEntity() instanceof Phantom) {
                e.setCancelled(true);
                Bukkit.getLogger().info("[MineralContest][INFO] Blocked a phantom spawn");
            }

            // TODO
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if(e.getEntity() instanceof Monster || e.getEntity() instanceof Mob) {
                if (partie != null && partie.isGameStarted()) {
                    if (Radius.isBlockInRadius(partie.getArene().getCoffre().getPosition(), e.getEntity().getLocation(), 60)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
