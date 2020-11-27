package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCAirDropSpawnEvent extends MCEvent {
    private Game game;
    private Location dropLocation;



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



}
