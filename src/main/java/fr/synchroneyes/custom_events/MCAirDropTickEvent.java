package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCAirDropTickEvent extends MCEvent {


    private Game game;
    private int timeLeft;


    public MCAirDropTickEvent(int timeLeft, Game partie) {
        this.timeLeft = timeLeft;
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }

    public int getTimeLeft() {
        return timeLeft;
    }


}
