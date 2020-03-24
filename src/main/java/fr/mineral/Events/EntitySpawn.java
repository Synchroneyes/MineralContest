package fr.mineral.Events;

import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) throws Exception {
        World worldEvent = e.getEntity().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            if(e.getEntity() instanceof Phantom) {
                e.setCancelled(true);
                Bukkit.getLogger().info("[INFO] Blocked a phantom spawn");
            }

            if(e.getEntity() instanceof Monster || e.getEntity() instanceof Mob) {
                if(mineralcontest.plugin.getGame().isGameStarted()){
                    if (Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), e.getEntity().getLocation(), 60)) {
                        e.setCancelled(true);
                    }
                }

            }
        }
    }
}
