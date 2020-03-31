package fr.mineral.Events;

import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
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
                if(event.getEntity() instanceof Player) {
                    Player victime = (Player) event.getEntity();

                    Player attaquant = null;

                    if(event.getDamager() instanceof Arrow) {
                        Arrow arrow = (Arrow) event.getDamager();
                        if(arrow.getShooter() instanceof Player) {
                            attaquant = (Player) arrow.getShooter();
                        }
                    }

                    if(event.getDamager() instanceof Player)  {
                        attaquant = (Player) event.getDamager();
                    }

                    if(attaquant != null) {
                        if(PlayerUtils.isPlayerInHisBase(victime) || PlayerUtils.isPlayerInHisBase(attaquant)) event.setCancelled(true);
                    }

                    if(PlayerUtils.isPlayerInDeathZone(victime))
                        event.setCancelled(true);

                    if((attaquant != null && PlayerUtils.isPlayerInHisBase(attaquant)) || PlayerUtils.isPlayerInHisBase(victime))
                        event.setCancelled(true);

                    if(attaquant != null) {
                        if(Radius.isBlockInRadius(mineralcontest.plugin.getGame().getArene().getTeleportSpawn(), attaquant.getLocation(), 5 )){
                            event.setCancelled(true);
                        }
                    }

                }
            }
        }
    }
}
