package fr.synchroneyes.special_events.halloween2024;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

/**
 * Fonction appelée lors de la mort d'un joueur
 */
public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathByPlayerEvent event) {

        Game partie = event.getPartie();

        if(partie == null) return;

        GameSettings parametres = partie.groupe.getParametresPartie();
        boolean halloweenEnabled = (parametres.getCVAR("enable_halloween_event").getValeurNumerique() == 1);

        // On désactive l'event halloween pour l'instant
        halloweenEnabled = false;
        if(!halloweenEnabled) return;

        DeathAnimation animationMort = new HalloweenHurricaneAnimation();
        animationMort.playAnimation(event.getPlayerDead());


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


    private Sound getRandomSound() {
        Sound[] sounds = new Sound[]{Sound.ENTITY_ZOMBIE_AMBIENT, Sound.ENTITY_CREEPER_PRIMED, Sound.BLOCK_STONE_STEP, Sound.ENTITY_SPIDER_AMBIENT, Sound.ENTITY_SPIDER_STEP, Sound.ENTITY_ENDERMAN_AMBIENT, Sound.ENTITY_ENDERMAN_TELEPORT, Sound.ENTITY_ENDERMAN_SCREAM};

        int random = new Random().nextInt(sounds.length);
        return sounds[random];
    }

}
