package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCShopItemPurchaseEvent extends Event {
    private Player joueur;
    private ShopItem item;

    private static final HandlerList handlers = new HandlerList();

    public MCShopItemPurchaseEvent(ShopItem item, Player acheteur) {
        this.item = item;
        this.joueur = acheteur;
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

    public ShopItem getItem() {
        return item;
    }
}
