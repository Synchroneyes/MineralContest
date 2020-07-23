package fr.synchroneyes.world_downloader;

import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.world_downloader.Inventories.InventoryInterface;
import fr.synchroneyes.world_downloader.Items.ItemInterface;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEvent implements Listener {

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        Player joueur = (Player) event.getWhoClicked();
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;
        if (mineralcontest.getPlayerGame(joueur) != null) {

            Inventory inventaireOuvert = event.getClickedInventory();

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getItemMeta() == null) return;

            WorldDownloader worldDownloader = WorldDownloader.getInstance();
            // Si on est sur l'inventaire de base de l'arbitre
            if (inventaireOuvert.equals(worldDownloader.getInventory())) {
                // On vérifie si on click sur un menh à ouvrir
                for (InventoryInterface inventaire : worldDownloader.inventaires) {
                    if (inventaire.isRepresentedItemStack(clickedItem)) {
                        inventaire.openInventory(joueur);
                        event.setCancelled(true);
                        return;
                    }
                }

                // Sinon, on a clické sur un item
                for (ItemInterface item : worldDownloader.items) {
                    if (item.toItemStack().equals(clickedItem)) {
                        item.performClick(joueur);
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            for (InventoryInterface inventaireCustom : worldDownloader.inventaires) {
                if (inventaireCustom.isEqualsToInventory(inventaireOuvert)) {
                    for (ItemInterface item : inventaireCustom.getObjets())
                        if (item.toItemStack().equals(clickedItem)) {
                            item.performClick(joueur);
                            event.setCancelled(true);
                            return;
                        }
                }
            }

            // Sinon, il faut vérifier si l'inventaire est un inventaire custom

        }
    }

}
