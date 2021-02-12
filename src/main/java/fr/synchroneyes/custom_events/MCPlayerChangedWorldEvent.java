package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.World;
import org.bukkit.event.Cancellable;

/**
 * Evenement appelé lors du changement de monde d'un joueur
 */
public class MCPlayerChangedWorldEvent extends MCEvent implements Cancellable {


    // Monde d'où vient le joueur
    private World from;

    // Monde où va le joueur
    private World to;

    private MCPlayer joueur;

    private boolean event_cancelled = false;

    public MCPlayerChangedWorldEvent(World from, World to, MCPlayer joueur) {
        this.from = from;
        this.to = to;
        this.joueur = joueur;
    }

    @Override
    public boolean isCancelled() {
        return event_cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.event_cancelled = b;
    }

    public World getFrom() {
        return from;
    }

    public World getTo() {
        return to;
    }

    public MCPlayer getJoueur() {
        return joueur;
    }
}
