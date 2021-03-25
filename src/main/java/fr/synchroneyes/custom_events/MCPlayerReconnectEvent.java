package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.entity.Player;

/**
 * Event appelé lorsqu'un joueur se reconnecte au plugin
 */
public class MCPlayerReconnectEvent extends MCEvent{

    private MCPlayer player;

    public MCPlayerReconnectEvent(MCPlayer player) {
        this.player = player;
    }

    public MCPlayer getPlayer() {
        return player;
    }
}
