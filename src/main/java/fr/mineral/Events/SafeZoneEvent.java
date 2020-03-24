package fr.mineral.Events;

import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class SafeZoneEvent implements Listener {

    @EventHandler
    public void onAttack( EntityDamageByEntityEvent event) throws Exception {
        World worldEvent = event.getEntity().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()) {
                if(event.getDamager() instanceof Player) {
                    Player p = (Player) event.getDamager();

                    Player joueur = (Player) event.getDamager();
                    if(PlayerUtils.isPlayerInDeathZone(joueur))
                        event.setCancelled(true);

                    if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getTeleportSpawn(), p.getLocation(), 5 )){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
