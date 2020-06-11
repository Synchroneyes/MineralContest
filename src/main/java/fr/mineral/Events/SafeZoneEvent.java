package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
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
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            if (!(event.getEntity() instanceof Player)) return;
            Player joueur = (Player) event.getEntity();
            Game partie = mineralcontest.getPlayerGame(joueur);
            if (partie != null && partie.isGameStarted()) {
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
                        if (Radius.isBlockInRadius(partie.getArene().getTeleportSpawn(), attaquant.getLocation(), partie.groupe.getParametresPartie().getCVAR("arena_safezone_radius").getValeurNumerique())) {
                            event.setCancelled(true);
                        }
                    }

                }
            }
        }
    }
}
