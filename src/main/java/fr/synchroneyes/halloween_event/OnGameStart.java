package fr.synchroneyes.halloween_event;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.DeathAnimations.Animations.GroundFreezingAnimation;
import fr.synchroneyes.mineral.DeathAnimations.Animations.HalloweenHurricaneAnimation;
import fr.synchroneyes.mineral.DeathAnimations.DeathAnimation;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class OnGameStart implements Listener {

    /**
     * On block le temps du monde quand la game démarre
     * @param event
     */
    @EventHandler
    public void OnGameStart(MCGameStartedEvent event) {
        // ON vérifie si halloween est actif ou non
        boolean halloweenEnabled = false;

        Game partie = event.getGame();
        GameSettings parametres = partie.groupe.getParametresPartie();
        halloweenEnabled = (parametres.getCVAR("enable_halloween_event").getValeurNumerique() == 1);


        // On force la désactivation d'halloween pour l'intant
        halloweenEnabled = false;

        if(halloweenEnabled) {

            String unknownName = ChatColor.RED + "???";

            World gameWorld = event.getGame().groupe.getMonde();
            FreezeWorldTime.setFrozenWorld(gameWorld);
            FreezeWorldTime.freezeWorld();




            // On joue les tâches de début de game
            sendDelayedTitleToEveryOne(ChatColor.WHITE + "\u2620 " + ChatColor.RED + "Mineral" + ChatColor.RED +" Contest" + ChatColor.WHITE + " \u2620", "Mode Halloween " + ChatColor.GREEN + "activé!", 5, 5, true, event.getGame());

            // Au bout de 23 secondes
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Je suis présent pour vous jouer de mauvais tours...", 5, 23, true, event.getGame());

            // à la 37ème seconde
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Je viens de tuer le soleil ...", 5, 37, true, event.getGame());

            // Au bout de 5 mn
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "N'ayez pas peur ...", 5, 5*60, true, event.getGame());



            // Lié à N'ayez Pas Peur, à la 5ème minute
            boolean finalHalloweenEnabled = halloweenEnabled;
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {

                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;
                for(Player joueur : partie.groupe.getPlayers()) {
                    joueur.playSound(joueur.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, .8f, 1);
                }
            }, 5*60*20);


            // Event Random à la 10ème minute, freeze du sol
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;

                for(Player joueur : partie.groupe.getPlayers()) {
                    joueur.playSound(joueur.getLocation(), Sound.BLOCK_GLASS_BREAK, .8f, 1);
                    joueur.sendTitle(ChatColor.GOLD + "???: " , ChatColor.BLUE + " Il fait un peu froid non ?", 20, 5*20, 20);
                    DeathAnimation animationMort = new GroundFreezingAnimation();
                    animationMort.playAnimation(joueur);

                }
            }, 10*60*20);

            // Faire apparaitre un ender squelette à la 17ème minute
            sendDelayedTitleToEveryOne(unknownName, "Attention où vous marchez..", 5, 17*60, true, partie);
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;

                for(Player joueur : partie.groupe.getPlayers()) {

                    WitherSkeleton babyZombie = joueur.getWorld().spawn(joueur.getLocation(), WitherSkeleton.class);
                    babyZombie.setCustomNameVisible(true);
                    babyZombie.setCustomName("Luss");
                    babyZombie.setHealth(1);
                    babyZombie.setAI(false);

                }
            }, 17*60*20);

            // Faire apparaitre des bébé zombie sur chaque joueur à la 25ème minute
            sendDelayedTitleToEveryOne(unknownName, "Nourissez vous mes petits.", 5, 25*60, true, event.getGame());
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;

                for(Player joueur : partie.groupe.getPlayers()) {

                    Zombie babyZombie = joueur.getWorld().spawn(joueur.getLocation(), Zombie.class);
                    babyZombie.setBaby();
                    babyZombie.setCustomNameVisible(true);
                    babyZombie.setCustomName("Pgjgj");
                    babyZombie.setHealth(1);
                    babyZombie.setAI(true);

                }
            }, 25*60*20);


            // 34ème minute
            // On aveugle les joueurs, et on met une citrouille sur la tête des joueurs à la 34eme minute, pendant 5 secondes
            sendDelayedTitleToEveryOne(unknownName, "Besoin d'un opticien?", 5, 34*60, false, event.getGame());
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;

                // On prépare la sauvegarde des anciens casques
                // On stock les casques actuels
                for(Player joueur : partie.groupe.getPlayers()){
                    ItemStack old_casque = joueur.getInventory().getHelmet();

                    // SI il n'avait pas de casque

                    // On crée le casque avec les données de l'ancien
                    ItemStack casque = null;

                    if(old_casque != null) {
                        casque = new ItemStack(old_casque.getType());
                        casque.setData(old_casque.getData());
                        casque.setItemMeta(old_casque.getItemMeta());
                        casque.setDurability(casque.getDurability());
                    }


                    // On applique le nouveau casque
                    ItemStack casque_halloween = new ItemStack(Material.CARVED_PUMPKIN);
                    joueur.getInventory().setHelmet(casque_halloween);

                    // ON remet son ancien casque dans 10 secs
                    ItemStack finalCasque = casque;
                    Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                        joueur.getInventory().setHelmet(finalCasque);
                    }, 10*20);
                }

            }, 34*60*20);

            // On fait spawn un enderman innoffensif en face des joueurs :), à la 44ème minute
            sendDelayedTitleToEveryOne(unknownName, "Bon courage avec mon ami...", 5, 44*60, false, event.getGame());
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                if(parametres.getCVAR("enable_halloween_event").getValeurNumerique() != 1) return;

                // Pour chaque joueur
                for(Player joueur : partie.groupe.getPlayers()) {
                    Enderman enderman = joueur.getWorld().spawn(joueur.getLocation(), Enderman.class);
                    enderman.setAI(false);
                    enderman.setCustomNameVisible(true);
                    enderman.setCustomName("Vezzen_");
                }
            }, 44*60*20);




        }
    }

    private void sendDelayedTitleToEveryOne(String title, String message, int duree_seconde, int duree_avant_annonce, boolean blindPlayers, Game game) {

        Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
            GameSettings parametres = game.groupe.getParametresPartie();
            boolean halloweenEnabled = (parametres.getCVAR("enable_halloween_event").getValeurNumerique() == 1);

            if(!halloweenEnabled) return;

            for(Player joueur : game.groupe.getPlayers()) {
                joueur.sendTitle(title, message, 20, 20*duree_seconde, 20);
                if(blindPlayers) joueur.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*(duree_seconde+2), 5));
            }
        }, duree_avant_annonce*20);
    }
}
