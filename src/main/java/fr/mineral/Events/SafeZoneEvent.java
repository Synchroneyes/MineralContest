package fr.mineral.Events;

import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;


public class SafeZoneEvent implements Listener {

    Radius radius = new Radius();

    public SafeZoneEvent(){

    }

    @EventHandler
    public void onAttack( EntityDamageByEntityEvent event) throws Exception {
        Entity p = event.getDamager();
        p.sendMessage("interact");

        if(Radius.isBlockInRadius(mineralcontest.plugin.getAreneLocation(), p.getLocation(), 5 )){
            event.setCancelled(true);
        }
    }
}
