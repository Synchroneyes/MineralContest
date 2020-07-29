package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCArenaChestSpawnEvent extends Event {
    private Game game;

    private static final HandlerList handlers = new HandlerList();

    public MCArenaChestSpawnEvent(Game partie) {
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
