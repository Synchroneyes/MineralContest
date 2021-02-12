package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerLeaveWorldPluginEvent;
import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Classe gérant les évenements lorsqu'un joueur quitte un monde où le plugin tourne
 */
public class PlayerLeavePluginWorld implements Listener {

    @EventHandler
    public void onPlayerLeavePluginWorld(MCPlayerLeaveWorldPluginEvent event) {
        // On récupère le joueur
        // On lui retire le scoreboard actuel
        event.getJoueur().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        // On marque le joueur comme quoi il a quitté le plugin
        event.getMcPlayer().setInPlugin(false);
    }
}
