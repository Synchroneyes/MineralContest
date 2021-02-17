package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerLeavePluginEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Evenement appelé lorsqu'un joueur quitte le serveur ou le plugin
 */
public class MCPlayerLeavePlugin implements Listener {

    @EventHandler
    public void onPlayerDisconnect(MCPlayerLeavePluginEvent event) {
        // On traite seulement les déconnexions
        // On déconnecte le joueur
        event.getMcPlayer().disconnectPlayer();
    }


}
