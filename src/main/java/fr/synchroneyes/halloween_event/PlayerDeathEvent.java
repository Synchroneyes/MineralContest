package fr.synchroneyes.halloween_event;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fonction appelée lors de la mort d'un joueur
 */
public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathByPlayerEvent event) {
        List<Entity> list_chauve_souris = new ArrayList<>();

        // Si le jouuer a bien été tué par quelqu'un
        if(event.getKiller() != null) {
            // On joue les animations
            // On fait apparaitre des chauves souris
            int nb_chauve_souris = 25;

            Location chaube_souris_Location = event.getKiller().getLocation().clone();
            chaube_souris_Location.setY(chaube_souris_Location.getBlockY() + 1);

            for(int i = 0; i < nb_chauve_souris; ++i) {
                // On crée une chauve souris
                Bat chauve_souris = (Bat) event.getPlayerDead().getWorld().spawnEntity(chaube_souris_Location, EntityType.BAT);

                // ON la rend "glowing"
                chauve_souris.setGlowing(true);
                chauve_souris.setCollidable(true);
                chauve_souris.setSilent(true);
                list_chauve_souris.add(chauve_souris);
            }

            // On fait également un coup de tonerre si le joueur est dehors
            // Pour determiner ça, on regarde si 15 blocs au dessus de lui c'est de l'air
            int nb_bloc_plafond = 20;
            Location copiedPlayerLocation = event.getPlayerDead().getLocation().clone();

            boolean isUnderRoof = false;
            // On regarde 20 blocs en dessous
            for(int y = 0; y < nb_bloc_plafond; ++y) {
                int tmpY = copiedPlayerLocation.getBlockY();
                copiedPlayerLocation.setY(tmpY+1);

                if(copiedPlayerLocation.getBlock().getType() != Material.AIR) {
                    isUnderRoof = true;
                    break;
                }
            }

            // Si on est pas sous un toit, on peut jouer l'effet de tonnerre
            if(!isUnderRoof) {
                event.getPlayerDead().getWorld().strikeLightningEffect(event.getPlayerDead().getLocation());
            }

            AreaEffectCloud e = (AreaEffectCloud) event.getPlayerDead().getWorld().spawnEntity(event.getPlayerDead().getLocation(), EntityType.AREA_EFFECT_CLOUD);
            e.setColor(Color.ORANGE);
            e.setDuration(4*20);

            AreaEffectCloud e1 = (AreaEffectCloud) event.getPlayerDead().getWorld().spawnEntity(event.getPlayerDead().getLocation(), EntityType.AREA_EFFECT_CLOUD);
            e1.setColor(Color.BLACK);
            e1.setDuration(4*20);

            // On démarre le timer des chauves souris
            // Durée en seconde du timer
            AtomicInteger temps_chauve_souris = new AtomicInteger(6);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(temps_chauve_souris.get() == 0) {

                        for(Entity chauve_sours : list_chauve_souris)
                            chauve_sours.remove();

                        this.cancel();
                        list_chauve_souris.clear();
                        return;
                    }

                    temps_chauve_souris.decrementAndGet();
                }
            }.runTaskTimer(mineralcontest.plugin, 0, 20);

            // On récupère le groupe
            Groupe playerGroup = mineralcontest.getPlayerGroupe(event.getKiller());
            if(playerGroup == null) return;

            // Pour chaque joueur du groupe
            for(Player joueurGroupe : playerGroup.getPlayers()) {
                // Si il n'est pas arbitre
                if(!playerGroup.getGame().isReferee(joueurGroupe) && !joueurGroupe.equals(event.getKiller()) && !joueurGroupe.equals(event.getPlayerDead())) {
                    // On lui joue un son
                    joueurGroupe.playSound(joueurGroupe.getLocation(), getRandomSound(), 0.8f, 1);
                }
            }

        }

    }

    private Sound getRandomSound() {
        Sound[] sounds = new Sound[]{Sound.ENTITY_ZOMBIE_AMBIENT, Sound.ENTITY_CREEPER_PRIMED, Sound.BLOCK_STONE_STEP, Sound.ENTITY_SPIDER_AMBIENT, Sound.ENTITY_SPIDER_STEP, Sound.ENTITY_ENDERMAN_AMBIENT, Sound.ENTITY_ENDERMAN_TELEPORT, Sound.ENTITY_ENDERMAN_SCREAM};

        int random = new Random().nextInt(sounds.length);
        return sounds[random];
    }

}
