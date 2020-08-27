package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.PlayerDeathByPlayerEvent;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.Statistics.Class.KillStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {


    /**
     * FOnction appelé à la mort du joueur
     */
    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player joueur = event.getEntity();

        // On vérifie que le joueur fasse partie du plugin
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            // Le joueur fait parti du plugin, on vérifie si il est dans une partie ou non
            Game partie = mineralcontest.getPlayerGame(joueur);

            if (partie == null) return;

            // Il est dans une partie, on vérifie si elle a démarré ou non
            if (partie.isGameStarted()) {

                if(joueur.getKiller() == null) event.setDeathMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_died.toString(), joueur));
                else event.setDeathMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.player_killed.toString(), joueur, joueur.getKiller()));

                PlayerDeathByPlayerEvent event1 = new PlayerDeathByPlayerEvent(joueur, joueur.getKiller(), partie);
                Bukkit.getPluginManager().callEvent(event1);

                event.getDrops().clear();

                MCPlayer mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);

                // On execute ces actions 1 tick plus tard
                Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                    // La partie est démarrée, on remet le joueur en vie, on l'ajoute à la deathzone, et on shoot l'event killed

                    partie.getArene().getDeathZone().add(joueur);
                    if(mcPlayer != null) {
                        mcPlayer.cancelDeathEvent();
                    }

                }, 1);


                // On enregistre les stats, au cas où ...
                if (joueur.getKiller() != null) {
                    // Si le joueur a été tué par un autre joueur
                    partie.getStatsManager().register(KillStat.class, joueur, joueur.getKiller());
                } else {

                    // Si le joueur s'est suicidé
                    partie.getStatsManager().register(KillStat.class, joueur, joueur);

                }

            }


        }
    }
}
