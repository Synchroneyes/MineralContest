package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {

    @EventHandler
    public void OnEntityTarget(EntityTargetEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);
    }
}
