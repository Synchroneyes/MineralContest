package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class EntityDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (partie == null) return;
            if (event.getEntity() instanceof Player && partie.isGameStarted()) {
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
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), victime), partie.groupe);
                    }
                }
            }
        }

    }


    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) throws Exception {

        World worldEvent = event.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (event.getEntity() instanceof Player && partie != null && partie.isGameStarted()) {
                Player victime = (Player) event.getEntity();
                Player attaquant = null;
                // We will check If they are both on the same team
                if (event.getDamager() instanceof Player) {
                    attaquant = (Player) event.getDamager();
                } else if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    if (arrow.getShooter() instanceof Player) {
                        attaquant = (Player) arrow.getShooter();
                    }
                }

                // Do the check
                if (attaquant != null) {
                    Equipe attaquantTeam = partie.getPlayerTeam(attaquant);
                    Equipe victimeTeam = partie.getPlayerTeam(victime);
                    int enable_friendly_fire = (int) GameSettingsCvarOLD.getValueFromCVARName("mp_enable_friendly_fire");
                    if (attaquantTeam.equals(victimeTeam) && enable_friendly_fire == 0) {
                        event.setCancelled(true);
                        return;
                    }
                }

                if (event.getDamager() instanceof Player && partie.isReferee((Player) event.getDamager())) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.isReferee(victime)) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.getArene().getDeathZone().isPlayerDead(victime)) {
                    event.setCancelled(true);
                    return;
                }

                if (partie.getArene().getCoffre().openingPlayer != null && partie.getArene().getCoffre().openingPlayer.equals(victime))
                    partie.getArene().getCoffre().close();

                // Si une entité meurt d'un coup/explosion/...
                if (victime.getHealth() - event.getDamage() < 0) {
                    victime.setHealth(20D);
                    event.setCancelled(true);

                    // Si c'est un joueur qui a tué notre victime
                    if (event.getDamager() instanceof Player) {
                        registerKill(victime, (Player) event.getDamager());
                    }

                    PlayerUtils.killPlayer(victime);

                }

            } else {
                event.setCancelled(true);
            }
            return;
        }
    }

    private void registerKill(Player dead, Player attacker) {
        if (mineralcontest.getPlayerGame(dead) == null) return;
        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), dead, attacker), mineralcontest.getPlayerGame(dead).groupe);

        mineralcontest.getPlayerGame(dead).killCounter++;
    }
}
