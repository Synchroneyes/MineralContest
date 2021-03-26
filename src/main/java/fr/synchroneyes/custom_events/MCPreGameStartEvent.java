package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Game.Game;
import org.bukkit.event.Cancellable;

public class MCPreGameStartEvent  extends MCEvent implements Cancellable {

    private boolean cancelled;
    private Game partie;

    public MCPreGameStartEvent(Game partie) {
        this.partie = partie;
    }

    public Game getPartie() {
        return partie;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
