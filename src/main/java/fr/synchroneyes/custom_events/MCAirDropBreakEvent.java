package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.Location;

public class MCAirDropBreakEvent extends MCEvent{

    private Game game;
    private Location dropLocation;

    public MCAirDropBreakEvent(Game partie, Location drop) {
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
