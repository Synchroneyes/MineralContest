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
    public void onPlayerDamage(EntityDamageEvent event) {
        if (mineralcontest.plugin.getGame().isGameStarted() && event.getEntity() instanceof Player) {
            Player victime = (Player) event.getEntity();


            if (victime.getHealth() - event.getDamage() < 0) {
                victime.setHealth(20D);
                event.setCancelled(true);
                PlayerUtils.killPlayer(victime);

                if(event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                    EntityDamageByEntityEvent event1 = (EntityDamageByEntityEvent) event;
                    if(event1.getDamager() instanceof Player) {
                        registerKill(victime, (Player) event1.getDamager());
                    }
                } else {
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), victime));
                }
            }
        }
    }


    @EventHandler
    public boolean onEntityDamage(EntityDamageByEntityEvent event) {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
                if(event.getEntity() instanceof  Player) {
                    Player victime = (Player) event.getEntity();

                    if(event.getDamager() instanceof Player && mineralcontest.plugin.getGame().isReferee((Player) event.getDamager())) {
                        event.setCancelled(true);
                        return true;
                    }

                    if(mineralcontest.plugin.getGame().isReferee(victime)){
                        event.setCancelled(true);
                        return true;
                    }

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
                            registerKill(victime, (Player) event.getDamager());
                        }

                        PlayerUtils.killPlayer(victime);

                    }
                }
        } else {
            event.setCancelled(true);
        }
        return false;
    }

    private void registerKill(Player dead, Player attacker) {
        mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), dead, attacker));
        mineralcontest.plugin.getGame().killCounter++;
    }
}
