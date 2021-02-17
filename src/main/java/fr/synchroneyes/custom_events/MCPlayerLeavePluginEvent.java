package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Evenement appelé lorsque le joueur quitte le plugin, lorsqu'il se déconnecte par exemple
 */
public class MCPlayerLeavePluginEvent extends MCEvent{

    private Player joueur;

    private MCPlayer mcPlayer;


    public MCPlayerLeavePluginEvent(Player joueur, MCPlayer mcPlayerInstance) {
        this.joueur = joueur;
        this.mcPlayer = mcPlayerInstance;
    }



    public MCPlayer getMcPlayer() {
        return mcPlayer;
    }

    public Player getJoueur() {
        return joueur;
    }
}
