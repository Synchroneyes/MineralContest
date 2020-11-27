package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Evenement symbolisant la fin d'une partie
 */
public class MCGameEndEvent extends MCEvent {


    private Game partie;




    public MCGameEndEvent(Game partie) {
        this.partie = partie;
    }


    public Game getGame() {
        return partie;
    }

}
