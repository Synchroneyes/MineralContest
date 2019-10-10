package fr.mineral.Events;

import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteract implements Listener {

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);

    }
}
