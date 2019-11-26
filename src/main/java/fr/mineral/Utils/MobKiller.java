package fr.mineral.Utils;

import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;

public class MobKiller {
    public static void killMobNearArena(int radius) throws Exception {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
            int entityCount = 0;
            for(World world: Bukkit.getServer().getWorlds()) {
                for(Entity e : world.getEntities()) {
                    if(e instanceof Monster || e instanceof Mob)
                        if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getCoffre().getPosition(), e.getLocation(), radius)) {
                            entityCount++;
                            e.remove();
                        }
                }
            }

            Bukkit.getLogger().info("[MobKiller] Removed " + entityCount + " entities near the arena");
        }else{
            Bukkit.getLogger().info("Could not execute this function: game not started");
        }
    }
}
