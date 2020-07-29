package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCArenaChestTickEvent extends Event {

    private Game game;
    private int timeLeft;


    private static final HandlerList handlers = new HandlerList();

    public MCArenaChestTickEvent(int timeLeft, Game partie) {
        this.timeLeft = timeLeft;
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }

    public int getTimeLeft() {
        return timeLeft;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
