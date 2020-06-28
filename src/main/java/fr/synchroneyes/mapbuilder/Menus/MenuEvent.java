package fr.synchroneyes.mapbuilder.Menus;

import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mapbuilder.Menus.Objets.ObjetTemplate;
import fr.synchroneyes.mapbuilder.Menus.SousMenu.MenuTemplate;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuEvent implements Listener {

    /**
     * Evenement appelé lors d'un click sur un item, on ouvrira par ce click, un menu ou on effectuera une action
     *
     * @param event
     */
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();

            // Si on est bien sur le plugin & que le mode est activé
            if (mineralcontest.isInAMineralContestWorld(joueur) && MapBuilder.getInstance().isBuilderModeEnabled) {

                // On regarde si l'item clické est un menu ou un inventaire
                if (event.getCurrentItem() == null) return;
                ItemStack clickedItem = event.getCurrentItem();

                if (event.getInventory().equals(MapBuilder.getInstance().getMenuManager().getInventaire())) {
                    for (MenuTemplate menu : MapBuilder.getInstance().getMenuManager().getMenus()) {
                        if (menu.toItemStack().equals(clickedItem)) {
                            menu.openInventory(joueur);
                            event.setCancelled(true);
                            return;
                        }
                    }

                    for (ObjetTemplate objet : MapBuilder.getInstance().getMenuManager().getObjets()) {
                        if (objet.toItemStack().equals(clickedItem)) {
                            objet.onClick(joueur);
                            event.setCancelled(true);
                            return;
                        }
                    }

                }


                for (MenuTemplate menu : MapBuilder.getInstance().getMenuManager().getMenus()) {
                    if (menu.getInventaire().equals(event.getClickedInventory())) {
                        Bukkit.getLogger().info("OK !");
                        for (ObjetTemplate objet : menu.getObjets()) {
                            if (objet.toItemStack().equals(clickedItem)) {
                                objet.onClick(joueur);
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }

                // Sinon, c'est un objet
                for (ObjetTemplate objet : MapBuilder.getInstance().getMenuManager().getObjets()) {
                    if (objet.toItemStack().equals(clickedItem)) {
                        objet.onClick(joueur);
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
