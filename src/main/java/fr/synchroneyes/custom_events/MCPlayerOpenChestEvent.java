package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCPlayerOpenChestEvent extends Event {

    private Player joueur;

    private AutomatedChestAnimation coffre;

    private static final HandlerList handlers = new HandlerList();

    public MCPlayerOpenChestEvent(AutomatedChestAnimation coffre, Player joueur) {
        this.joueur = joueur;
        this.coffre = coffre;
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

    public AutomatedChestAnimation getCoffre() {
        return coffre;
    }
}
