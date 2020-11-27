package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCArenaChestSpawnEvent extends MCEvent {
    private Game game;

    public MCArenaChestSpawnEvent(Game partie) {
        this.game = partie;
    }

    public Game getGame() {
        return game;
    }

}
