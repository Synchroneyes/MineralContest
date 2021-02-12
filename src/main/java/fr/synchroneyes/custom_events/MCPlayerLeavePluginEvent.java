package fr.synchroneyes.custom_events;

import org.bukkit.entity.Player;

/**
 * Evenement appelé lorsque le joueur quitte le plugin, lorsqu'il se déconnecte par exemple
 */
public class MCPlayerLeavePluginEvent extends MCEvent{

    private Player joueur;

    public MCPlayerLeavePluginEvent(Player joueur) {
        this.joueur = joueur;
    }

    public Player getJoueur() {
        return joueur;
    }
}
