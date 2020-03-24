package fr.mineral.Events;

import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            Player joueur = event.getPlayer();
            Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
            House house = mineralcontest.plugin.getGame().getPlayerHouse(joueur);

            if(mineralcontest.plugin.getGame().isPlayerReady(joueur)) mineralcontest.plugin.getGame().removePlayerReady(joueur);

            if(joueur.isOp())
                if(mineralcontest.plugin.getGame().isReferee(joueur)) mineralcontest.plugin.getGame().removeReferee(joueur);


            if(team != null)
                team.removePlayer(joueur);

            if(mineralcontest.plugin.getGame().isGameStarted() && team != null) {
                mineralcontest.plugin.getGame().pauseGame();
                mineralcontest.plugin.getGame().addDisconnectedPlayer(joueur.getDisplayName(), team);
                house.getPorte().forceCloseDoor();
            }

            if(mineralcontest.plugin.getGame().votemap.voteEnabled) mineralcontest.plugin.getGame().votemap.removePlayerVote(joueur);

            int number_of_player_online = mineralcontest.plugin.pluginWorld.getPlayers().size() - 1;
            if(number_of_player_online == 0) {
                mineralcontest.plugin.getGame().resetMap();
            }
        }

    }
}
