package fr.mineral.Events;

import fr.mineral.Utils.PlayerUtils;
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

        Player joueur = (Player) event.getDamager();
        if(PlayerUtils.isPlayerInDeathZone(joueur))
            event.setCancelled(true);

        if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getTeleportSpawn(), p.getLocation(), 5 )){
            event.setCancelled(true);
        }
    }
}
