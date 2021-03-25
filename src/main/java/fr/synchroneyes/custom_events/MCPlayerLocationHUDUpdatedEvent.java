package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.event.Cancellable;

/**
 * Event appelé lorsqu'il faut changer la position du joueur sur le HUD
 */
public class MCPlayerLocationHUDUpdatedEvent extends MCEvent implements Cancellable {

    private MCPlayer player;
    private boolean cancelled;

    public MCPlayerLocationHUDUpdatedEvent(MCPlayer player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public MCPlayer getPlayer() {
        return player;
    }
}
