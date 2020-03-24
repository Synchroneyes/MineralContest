package fr.mineral.Events;

import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteract implements Listener {

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGamePaused())
                event.setCancelled(true);
        }


    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGamePaused())
                if(event.getDamager() instanceof Monster || event.getDamager() instanceof Arrow)
                    event.setCancelled(true);
        }

    }
}
