package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;

/**
 * Event appelé lorsqu'un joueur un monde du plugin, mais reste connecté
 */
public class MCPlayerLeaveWorldPluginEvent extends MCEvent{

    private Player joueur;

    private MCPlayer mcPlayer;

    public MCPlayerLeaveWorldPluginEvent(Player joueur) {
        this.joueur = joueur;
        mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
    }


    public Player getJoueur() {
        return joueur;
    }

    public MCPlayer getMcPlayer() {
        return mcPlayer;
    }
}
