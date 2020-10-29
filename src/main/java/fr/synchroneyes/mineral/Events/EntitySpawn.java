package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) throws Exception {
        World worldEvent = e.getEntity().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            if (e.getEntity() instanceof Phantom) {
                e.setCancelled(true);
                Bukkit.getLogger().info("[MineralContest][INFO] Blocked a phantom spawn");
                return;
            }

            // TODO
            Game partie = mineralcontest.getWorldGame(worldEvent);
            if (e.getEntity() instanceof Monster ||
                    e.getEntity() instanceof Mob &&
                    !(e.getEntity() instanceof Chicken)
                    && !(e.getEntity() instanceof Villager)
                    && !(e.getEntity() instanceof Horse)
                    && !(e.getEntity() instanceof ArmorStand)) {
                if (partie != null && partie.isGameStarted()) {
                    if (Radius.isBlockInRadius(partie.getArene().getCoffre().getLocation(), e.getEntity().getLocation(), partie.groupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique())) {


                        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                            // On est dans le rayon de l'arène
                            // On doit vérifier si on est dans les vagues de poulet
                            if(partie.getArene().chickenWaves.isFromChickenWave((LivingEntity) e.getEntity())) {
                                // Si oui, on ne fait rien
                                return;
                            }

                            // On vérifie maintenant si c'est une chauve souris!
                            if(e.getEntity() instanceof Bat) {
                                return;
                            }

                            // On vérifie maintenant si c'est le boss!
                            if(partie.getBossManager().isThisEntityABoss((LivingEntity) e.getEntity())) {
                                LivingEntity livingEntity = (LivingEntity) e.getEntity();
                                if(livingEntity.getCustomName() != null || livingEntity.getCustomName().length() > 1) {
                                    return;
                                }
                            }

                            if (partie.groupe.getParametresPartie().getCVAR("enable_monster_in_protected_zone").getValeurNumerique() != 1) {
                                e.setCancelled(true);
                            }
                        }, 1);


                    }
                }
            }
        }
    }
}
