package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCAirDropSpawnEvent extends Event {
    private Game game;
    private Location dropLocation;


    private static final HandlerList handlers = new HandlerList();

    public MCAirDropSpawnEvent(Location drop, Game partie) {
        this.dropLocation = drop;
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }

    public Location getParachuteLocation() {
        return dropLocation;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
