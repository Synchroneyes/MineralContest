package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evenement appelé lors de la mort par un autre joueur
 */
public class PlayerDeathByPlayerEvent extends Event {

    private Player playerDead;
    private Player killer;
    private Game partie;


    private static final HandlerList handlers = new HandlerList();

    public PlayerDeathByPlayerEvent(Player dead, Player killer, Game partie) {
        this.playerDead = dead;
        this.killer = killer;
        this.partie = partie;
    }

    public Game getPartie() {
        return partie;
    }

    public Player getPlayerDead() {
        return playerDead;
    }

    public Player getKiller() {
        return killer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
