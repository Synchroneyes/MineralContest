package fr.synchroneyes.mineral.DeathAnimations.Animations;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class HalloweenHurricaneAnimation extends DeathAnimation {
    @Override
    public String getAnimationName() {
        return "Tempête d'Halloween";
    }

    @Override
    public void playAnimation(LivingEntity entity) {
        List<Entity> list_chauve_souris = new ArrayList<>();

        int duree_animation = 3;

        // On joue les animations
        // On fait apparaitre des chauves souris
        int nb_chauve_souris = 25;

        Location chaube_souris_Location = entity.getLocation().clone();
        chaube_souris_Location.setY(chaube_souris_Location.getBlockY() + 1);

        for (int i = 0; i < nb_chauve_souris; ++i) {
            // On crée une chauve souris
            Bat chauve_souris = (Bat) entity.getWorld().spawnEntity(chaube_souris_Location, EntityType.BAT);

            // ON la rend "glowing"
            chauve_souris.setGlowing(true);
            chauve_souris.setCollidable(true);
            chauve_souris.setSilent(true);
            list_chauve_souris.add(chauve_souris);
        }

        // On fait également un coup de tonerre si le joueur est dehors
        // Pour determiner ça, on regarde si 15 blocs au dessus de lui c'est de l'air
        int nb_bloc_plafond = 20;
        Location copiedPlayerLocation = entity.getLocation().clone();

        boolean isUnderRoof = false;
        // On regarde 20 blocs en dessous
        for (int y = 0; y < nb_bloc_plafond; ++y) {
            int tmpY = copiedPlayerLocation.getBlockY();
            copiedPlayerLocation.setY(tmpY + 1);

            if (copiedPlayerLocation.getBlock().getType() != Material.AIR) {
                isUnderRoof = true;
                break;
            }
        }

        // Si on est pas sous un toit, on peut jouer l'effet de tonnerre
        /*if (!isUnderRoof) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
        }*/

        AreaEffectCloud e = (AreaEffectCloud) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        e.setColor(Color.ORANGE);
        e.setDuration(duree_animation * 20);

        AreaEffectCloud e1 = (AreaEffectCloud) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        e1.setColor(Color.BLACK);
        e1.setDuration(duree_animation * 20);

        // On démarre le timer des chauves souris
        // Durée en seconde du timer
        AtomicInteger temps_chauve_souris = new AtomicInteger(duree_animation+1);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (temps_chauve_souris.get() == 0) {

                    for (Entity chauve_sours : list_chauve_souris)
                        chauve_sours.remove();

                    this.cancel();
                    list_chauve_souris.clear();
                    return;
                }

                temps_chauve_souris.decrementAndGet();
            }
        }.runTaskTimer(mineralcontest.plugin, 0, 20);

    }

}
