package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class SpeedWorldLoading implements Listener {

    @EventHandler(priority= EventPriority.HIGHEST)
    public void enableSpeedWorldLoading(WorldInitEvent event) {
        World monde = event.getWorld();

        // Si c'est un monde mineral contest; on active le chargement rapide
        if(mineralcontest.isAMineralContestWorld(monde)) {
            monde.setKeepSpawnInMemory(false);
        }
    }
}
