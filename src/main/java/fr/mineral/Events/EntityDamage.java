package fr.mineral.Events;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {


    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (mineralcontest.plugin.getGame().isGameStarted() && event.getEntity() instanceof Player) {
            Player victime = (Player) event.getEntity();

            if (victime.getHealth() - event.getDamage() < 0) {
                victime.setHealth(20D);
                event.setCancelled(true);

                PlayerUtils.killPlayer(victime);

                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString()));

            }
        }
    }

    @EventHandler
    public boolean onEntityDamage(EntityDamageByEntityEvent event) {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
                if(event.getEntity() instanceof  Player) {
                    Player victime = (Player) event.getEntity();
                    if(mineralcontest.plugin.getGame().getArene().getDeathZone().isPlayerDead(victime)){
                        event.setCancelled(true);
                        return true;
                    }

                    // Si une entité meurt d'un coup/explosion/...
                    if (victime.getHealth() - event.getDamage() < 0) {
                        victime.setHealth(20D);
                        event.setCancelled(true);

                        // Si c'est un joueur qui a tué notre victime
                        if(event.getDamager() instanceof Player) {
                            Player attaquant = (Player) event.getDamager();
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), victime, attaquant));
                            mineralcontest.plugin.getGame().killCounter++;
                        }

                        PlayerUtils.killPlayer(victime);

                    }
                }
        }
        return false;
    }
}
