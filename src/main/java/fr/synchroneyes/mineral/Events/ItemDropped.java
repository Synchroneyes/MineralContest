package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDropped implements Listener {

    /**
     * Méthode appelée lorsqu'un joueur drop un item!
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // On récupère le joueur
        Player joueur = event.getPlayer();

        // On vérifie que le joueur fait partie du plugin
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            ItemStack droppedItem = event.getItemDrop().getItemStack();

            // Si c'est un item du shop
            if (ShopManager.isAnShopItem(droppedItem)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
