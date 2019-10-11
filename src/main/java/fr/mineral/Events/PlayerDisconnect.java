package fr.mineral.Events;

import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player joueur = event.getPlayer();
        Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(joueur);
        if(team != null)
            team.removePlayer(joueur);

        if(mineralcontest.plugin.getGame().isGameStarted()) {
            mineralcontest.plugin.getGame().pauseGame();
        }

        joueur.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);

    }
}
