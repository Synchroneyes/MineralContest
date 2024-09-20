package fr.synchroneyes.special_events.halloween2024;

import fr.synchroneyes.custom_events.MCGameStartedEvent;

import fr.synchroneyes.special_events.halloween2024.game_events.FakeChestEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.special_events.halloween2024.game_events.FightArenaEvent;
import fr.synchroneyes.special_events.halloween2024.game_events.HellParkourEvent;
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

            String unknownName = ChatColor.RED + "???";

            World gameWorld = event.getGame().groupe.getMonde();
            FreezeWorldTime.setFrozenWorld(gameWorld);
            FreezeWorldTime.freezeWorld();

            FakeChestEvent fakeChestEvent = new FakeChestEvent(event.getGame());
            FightArenaEvent fightArenaEvent = new FightArenaEvent(event.getGame());
            HellParkourEvent hellParkourEvent = new HellParkourEvent(event.getGame());

            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, hellParkourEvent::execute, 20*5);

            // On joue l'event Fight Arena au bout de 15 secondes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, fightArenaEvent::execute, 60*20*2);

            // On joue l'event Fake Chest au bout de 8 minutes
            Bukkit.getServer().getScheduler().runTaskLater(mineralcontest.plugin, fakeChestEvent::execute, 20*60*8);




            /*
                Features:
                - Fausse arène d'arène, avec des mobs à la place du coffre. Quand les mobs sont morts, lorsque le joueur tente d'ouvrir le coffre,
                 une animation se joue, puis le coffre se ferme, et se transforme en TNT

                - Boss de fin

                - Screamers



                Scénario Halloween 2024
                1- Lors du démarrage de la partie, tous les joueurs sont TP sur la fausse arène au loin, remplis de monstre, avec un Y+ 20 & Freeze
                2- Les joueurs sont aveuglé pendant 15 secondes, et un esprit leur parle
                3- les joueurs se fightent dans l'arène



             */


            // On joue les tâches de début de game
            sendDelayedTitleToEveryOne(ChatColor.WHITE + "\u2620 " + ChatColor.RED + "Mineral" + ChatColor.RED +" Contest" + ChatColor.WHITE + " \u2620", "Mode Halloween " + ChatColor.GREEN + "activé!", 5, 5, true, event.getGame());






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
