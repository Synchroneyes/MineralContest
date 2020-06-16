package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) throws Exception {
        World worldEvent = event.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Player joueur = event.getPlayer();
            Game partie = mineralcontest.getWorldGame(worldEvent);

            GameLogger.addLog(new Log("PlayerDisconnect", "Player " + joueur.getDisplayName() + " disconnected", "player_disconnect"));

            if (partie == null && mineralcontest.communityVersion) return;
            if (!mineralcontest.communityVersion && partie == null) {
                mineralcontest.plugin.getNonCommunityGroup().retirerJoueur(joueur);
                return;
            }

            Equipe team = partie.getPlayerTeam(joueur);
            House house = partie.getPlayerHouse(joueur);


            if (partie.isPlayerReady(joueur)) partie.removePlayerReady(joueur);

            if(joueur.isOp())
                if (partie.isReferee(joueur)) partie.removeReferee(joueur);




            if ((partie.isGameStarted() || partie.isPreGame()) && team != null) {
                partie.pauseGame();
                partie.groupe.addDisconnectedPlayer(joueur);
                house.getPorte().forceCloseDoor();
            }


            if (team != null)
                team.removePlayer(joueur);


            if (!partie.isGameStarted()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, () -> {
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.hud_awaiting_players.toString(), partie.groupe);
                }, 20);
            }


            Bukkit.getScheduler().runTaskLater(mineralcontest.plugin, () -> {
                int number_of_player_online = partie.groupe.getPlayers().size();
                if(number_of_player_online == 0) {
                    partie.resetMap();
                    if (partie.isGameStarted()) {
                        try {
                            partie.terminerPartie();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, partie);
                        }
                    }
                }
            }, 20);

            event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4d);


        }

    }
}
