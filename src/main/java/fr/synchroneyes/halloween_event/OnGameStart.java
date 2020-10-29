package fr.synchroneyes.halloween_event;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.DeathAnimations.Animations.GroundFreezingAnimation;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OnGameStart implements Listener {

    /**
     * On block le temps du monde quand la game démarre
     * @param event
     */
    @EventHandler
    public void OnGameStart(MCGameStartedEvent event) {
        // ON vérifie si halloween est actif ou non
        boolean halloweenEnabled = true;

        if(halloweenEnabled) {
            World gameWorld = event.getGame().groupe.getMonde();
            FreezeWorldTime.setFrozenWorld(gameWorld);
            FreezeWorldTime.freezeWorld();
            //Bukkit.broadcastMessage("Froze " + gameWorld + " - " + gameWorld.getName());

            // On joue les tâches de début de game
            sendDelayedTitleToEveryOne(ChatColor.WHITE + "\u2620 " + ChatColor.RED + "Mineral" + ChatColor.RED +" Contest" + ChatColor.WHITE + " \u2620", "Mode Halloween " + ChatColor.GREEN + "activé!", 5, 5, true, event.getGame());
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Je suis présent pour vous jouer de mauvais tours...", 5, 23, true, event.getGame());
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Je viens de tuer le soleil ...", 5, 37, true, event.getGame());
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "N'ayez pas peur ...", 5, 5*60, true, event.getGame());

            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                Game partie = event.getGame();
                for(Player joueur : partie.groupe.getPlayers()) {
                    joueur.playSound(joueur.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, .8f, 1);
                }
            }, 5*60*20);


            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                Game partie = event.getGame();
                for(Player joueur : partie.groupe.getPlayers()) {
                    joueur.playSound(joueur.getLocation(), Sound.BLOCK_GLASS_BREAK, .8f, 1);
                    joueur.sendTitle(ChatColor.GOLD + "???: " , ChatColor.BLUE + " Il fait un peu froid non ?", 20, 5*20, 20);
                    DeathAnimation animationMort = new GroundFreezingAnimation();
                    animationMort.playAnimation(joueur);

                }
            }, 10*60*20);


        }
    }

    private void sendDelayedTitleToEveryOne(String title, String message, int duree_seconde, int duree_avant_annonce, boolean blindPlayers, Game game) {
        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            for(Player joueur : game.groupe.getPlayers()) {
                joueur.sendTitle(title, message, 20, 20*duree_seconde, 20);
                if(blindPlayers) joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*(duree_seconde+2), 5));
            }
        }, duree_avant_annonce*20);
    }
}
