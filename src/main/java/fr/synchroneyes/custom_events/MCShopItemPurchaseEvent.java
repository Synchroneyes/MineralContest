package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MCShopItemPurchaseEvent extends MCEvent {
    private Player joueur;
    private ShopItem item;


    public MCShopItemPurchaseEvent(ShopItem item, Player acheteur) {
        this.item = item;
        this.joueur = acheteur;
    }

    public Player getJoueur() {
        return joueur;
    }

    public ShopItem getItem() {
        return item;
    }
}
