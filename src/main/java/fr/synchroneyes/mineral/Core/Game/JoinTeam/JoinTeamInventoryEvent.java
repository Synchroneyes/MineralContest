package fr.synchroneyes.mineral.Core.Game.JoinTeam;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.groups.Utils.Etats;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.Game.JoinTeam.Items.ItemInterface;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JoinTeamInventoryEvent implements Listener {

    /**
     * Méthode appelée lorsqu'un joueur clique sur un item
     * On vérifiera si c'est un item pour choisir une équipe
     * Si c'est le cas, on ajoutera le joueur dans l'équipe
     *
     * @param event
     */
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player joueur = (Player) event.getWhoClicked();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;

            if (playerGroup.getGame() == null || playerGroup.getMonde() == null || playerGroup.getEtatPartie() != Etats.ATTENTE_DEBUT_PARTIE)
                return;
            ItemStack clickedItem = event.getCurrentItem();
            Inventory clickedInventory = event.getInventory();


            if (playerGroup.getGame().getTeamSelectionMenu().getInventory() == null) return;
            // Si l'inventaire où on a effectué le click est l'inventaire de séléction d'équipe
            if (clickedInventory.equals(playerGroup.getGame().getTeamSelectionMenu().getInventory())) {

                // Pour chaque item de cet inventaire
                for (ItemInterface item : playerGroup.getGame().getTeamSelectionMenu().getItems()) {

                    // On regarde si l'item cliqué correspond à un item de l'inventaire "custom"
                    if (clickedItem != null && clickedItem.getItemMeta() != null && item.toItemStack().equals(clickedItem)) {

                        // On a cliqué sur un item
                        item.performClick(joueur);
                        event.setCancelled(true);
                        joueur.closeInventory();
                    }
                }

            }

        }
    }


    /**
     * Event appelée lorsque le joueur fait un clic en ayant le livre de séléction de temps en main
     *
     * @param event
     */
    @EventHandler
    public void onItemRightClick(PlayerInteractEvent event) {
        Player joueur = event.getPlayer();
        if (mineralcontest.isInAMineralContestWorld(joueur)) {
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);
            if (playerGroup == null) return;
            if (playerGroup.getGame() == null) return;

            ItemStack item = event.getItem();
            if (item != null && playerGroup.getEtatPartie() == Etats.ATTENTE_DEBUT_PARTIE && item.equals(Game.getTeamSelectionItem())) {
                playerGroup.getGame().openTeamSelectionMenuToPlayer(joueur);
                event.setCancelled(true);
                return;
            }
        }
    }

}
