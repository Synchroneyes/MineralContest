package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evenement envoyé lors du démarrage d'une game
 */
public class MCGameStartedEvent extends MCEvent {

    private Game game;

    public MCGameStartedEvent(Game partie) {
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }

}
