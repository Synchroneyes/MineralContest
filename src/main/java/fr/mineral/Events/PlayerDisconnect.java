package fr.mineral.Events;

import fr.mineral.Core.House;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player joueur = event.getPlayer();
        Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
        House house = mineralcontest.plugin.getGame().getPlayerHouse(joueur);

        if(team != null)
            team.removePlayer(joueur);

        if(mineralcontest.plugin.getGame().isGameStarted() && team != null) {
            mineralcontest.plugin.getGame().pauseGame();
            mineralcontest.plugin.getGame().addDisconnectedPlayer(joueur.getDisplayName(), team);
            house.getPorte().forceCloseDoor();
        }
    }
}
