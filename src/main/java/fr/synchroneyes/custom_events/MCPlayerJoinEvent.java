package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evenement envoyé lors du démarrage d'une game
 */
public class MCPlayerJoinEvent extends MCEvent {



    private MCPlayer mcPlayer;

    private Player player;

    public MCPlayerJoinEvent(Player p, MCPlayer mcPlayer) {
        this.player = p;
        this.mcPlayer = mcPlayer;
    }

    public MCPlayer getMcPlayer() {
        return mcPlayer;
    }

    public Player getPlayer() {
        return player;
    }

}
