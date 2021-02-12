package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.custom_events.MCPlayerJoinEvent;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


/**
 * Classe gérant l'arrivé d'un joueur dans le plugin
 */
public class PlayerJoinPlugin implements Listener {

    @EventHandler
    public void onPlayerJoinLobby(MCPlayerJoinEvent event) {
        // On l'ajoute au plugin
        mineralcontest.plugin.addNewPlayer(event.getPlayer());
        event.getPlayer().sendMessage("Bienvenue dans le plugin");
    }
}
