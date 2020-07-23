package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Kits.KitAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Evenement appelé lors de la selection d'un kit
 */
public class PlayerKitSelectedEvent extends Event {


    private Player player;
    private KitAbstract selectedKit;


    private static final HandlerList handlers = new HandlerList();

    public PlayerKitSelectedEvent(Player joueur, KitAbstract selectedKit) {
        this.player = joueur;
        this.selectedKit = selectedKit;
    }

    public Player getPlayer() {
        return player;
    }

    public KitAbstract getSelectedKit() {
        return selectedKit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
