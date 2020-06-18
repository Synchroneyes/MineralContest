package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.Referee.Referee;
import fr.mineral.Utils.Player.PlayerUtils;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSpawn implements Listener {


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) throws Exception {

        World worldEvent = e.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Game partie = mineralcontest.getWorldGame(worldEvent);

            if(partie != null)
                if(partie.isReferee(e.getPlayer()))
                    e.getPlayer().getInventory().setItemInMainHand(Referee.getRefereeItem());


            if (partie != null && partie.isGameStarted()) {
                // Si la game est démarrée
                Player joueur = e.getPlayer();
                // Si le joueur était dans la deathzone
                PlayerUtils.resetPlayerDeathZone(joueur);
                return;

            }


           new BukkitRunnable() {
                @Override
                public void run() {
                    // On le TP au centre de l'arène
                    if(partie != null && partie.groupe.getMonde() != null) {
                        Player joueur = e.getPlayer();
                        joueur.teleport(partie.groupe.getMonde().getSpawnLocation());
                    }
                }
            }.runTaskLater(mineralcontest.plugin, 20);


        }

    }
}
