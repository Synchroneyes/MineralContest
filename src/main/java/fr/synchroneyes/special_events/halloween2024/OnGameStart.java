package fr.synchroneyes.special_events.halloween2024;

import fr.synchroneyes.custom_events.MCGameStartedEvent;

import fr.synchroneyes.special_events.halloween2024.game_events.ArenaMonsterEvent;
import fr.synchroneyes.special_events.halloween2024.game_events.FakeChestEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2024.game_events.FightArenaEvent;
import fr.synchroneyes.special_events.halloween2024.game_events.HellParkourEvent;
import fr.synchroneyes.special_events.halloween2024.utils.Screamer;
import org.bukkit.*;
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
        boolean halloweenEnabled = false;

        Game partie = event.getGame();
        GameSettings parametres = partie.groupe.getParametresPartie();

        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Halloween 2024 activé!");

        halloweenEnabled = true;

        if(halloweenEnabled) {



            World gameWorld = event.getGame().groupe.getMonde();
            FreezeWorldTime.setFrozenWorld(gameWorld);
            FreezeWorldTime.freezeWorld();

            FakeChestEvent fakeChestEvent = new FakeChestEvent(event.getGame());
            FightArenaEvent fightArenaEvent = new FightArenaEvent(event.getGame());
            HellParkourEvent hellParkourEvent = new HellParkourEvent(event.getGame());
            ArenaMonsterEvent arenaMonsterEvent = new ArenaMonsterEvent(event.getGame());

            sendDelayedTitleToEveryOne(ChatColor.WHITE + "\u2620 " + ChatColor.RED + "Mineral" + ChatColor.RED +" Contest" + ChatColor.WHITE + " \u2620", "Mode Halloween " + ChatColor.GREEN + "activé!", 5, 5, true, event.getGame());

            // On joue l'event Son éclair au bout de 5 secondes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playThunderSound", partie), 20*5);

            // On joue l'event Fight Arena au bout de 5 secondes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, fightArenaEvent::execute, 20*5);

            // On joue l'event Son creeper au bout de 4 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playCreeperSound", partie), 20*60*4);

            // Titre joué après 2 minutes
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "N'ayez pas peur.. ce n'était que le début", 5, 60*2, false, event.getGame());


            // On joue l'event Warden au bout de 5 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playWarden", partie), 20*60*5);

            // Titre joué après 8 minutes 30
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Vous avez l'air trop en forme...", 5, 60*8+30, false, event.getGame());

            // On joue l'event Hell Parkour au bout de 9 minutes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, hellParkourEvent::execute, 20*60*9);

            // On joue l'event Son Warden au bout de 14 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playWardenSound", partie), 20*60*14);

            // On joue l'event Screamer au bout de 19 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playCreeper", partie), 20*60*8);


            // Titre joué après 19 minutes 10
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Peur d'exploser? :)", 5, 60*19+10, false, event.getGame());

            // On joue l'event Son Warden au bout de 14 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playWardenSound", partie), 20*60*14);

            // On joue l'event Fake Chest au bout de 28 minutes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, fakeChestEvent::execute, 20*60*28);

            // On joue l'event Pluie d'enclume au bout de 31 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playAnvilRain", partie), 20*60*31);

            // On joue l'event Monstre Random au bout de 36 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playMonsterRoulette", partie), 20*60*36);

            // On joue l'event Son Zombie au bout de 40 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playZombieSound", partie), 20*60*40);

            // On joue l'event Son Enderman au bout de 47 minutes
            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> Screamer.playEffectToAllPlayers("playEndermanSound", partie), 20*60*40);

            // Titre joué après 51 minutes 30
            sendDelayedTitleToEveryOne(ChatColor.RED + "???", "Ne vous inquietez pas, j'arrive ...", 5, 60*51+30, true, event.getGame());


            // On joue l'event arenaMonsterEvent au bout de 52 minutes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, arenaMonsterEvent::execute, 20*60*52);






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
