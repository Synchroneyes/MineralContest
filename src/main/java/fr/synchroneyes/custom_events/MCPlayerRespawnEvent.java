package fr.synchroneyes.custom_events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCPlayerRespawnEvent extends Event {


    private Player joueur;

    private static final HandlerList handlers = new HandlerList();

    public MCPlayerRespawnEvent(Player joueur) {
        this.joueur = joueur;
    }

    public Player getJoueur() {
        return joueur;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
