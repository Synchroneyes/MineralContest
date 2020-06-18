package fr.mineral.Core.Referee;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.Referee.Inventory.InventoryTemplate;
import fr.mineral.Core.Referee.Items.RefereeItem;
import fr.mineral.Core.Referee.Items.RefereeItemTemplate;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RefereeEvent implements Listener {

    @EventHandler
    public void OnPlayerRightClick(PlayerInteractEvent event) {
        Player joueur = event.getPlayer();

        if (mineralcontest.getPlayerGame(joueur) != null && mineralcontest.getPlayerGame(joueur).isReferee(joueur)) {
            if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                // On doit récup l'item
                ItemStack itemEnMain = joueur.getInventory().getItemInMainHand();
                ItemMeta itemEnMainMeta = itemEnMain.getItemMeta();

                if (!itemEnMain.getType().equals(Material.AIR) && itemEnMainMeta.getDisplayName().equals(Referee.getRefereeItem().getItemMeta().getDisplayName())) {
                    // C'est le livre d'arbitrage
                    joueur.openInventory(RefereeInventory.getInventory());
                }
            }
        }
    }

    @EventHandler
    public void OnInventoryItemClicked(InventoryClickEvent event) {
        Player joueur = (Player) event.getWhoClicked();
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;
        if (mineralcontest.getPlayerGame(joueur) != null && mineralcontest.getPlayerGame(joueur).isReferee(joueur)) {

            Inventory inventaireOuvert = event.getClickedInventory();

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getItemMeta() == null) return;

            // Si on est sur l'inventaire de base de l'arbitre
            if (inventaireOuvert.equals(RefereeInventory.getInventory())) {
                // On vérifie si on click sur un menh à ouvrir
                for (InventoryTemplate inventaire : RefereeInventory.inventaires) {
                    if (inventaire.isRepresentedItemStack(clickedItem)) {
                        inventaire.openInventory(joueur);
                        event.setCancelled(true);
                        return;
                    }
                }

                // Sinon, on a clické sur un item
                for (RefereeItemTemplate item : RefereeInventory.items) {
                    if (item.toItemStack().equals(clickedItem)) {
                        item.performClick(joueur);
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            for (InventoryTemplate inventaireCustom : RefereeInventory.inventaires) {
                if (inventaireCustom.isEqualsToInventory(inventaireOuvert)) {
                    for (RefereeItemTemplate item : inventaireCustom.getObjets())
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

    private void sendActionUnavailable(Player p) {
        p.sendMessage(mineralcontest.prefixErreur + Lang.referee_action_not_available.toString());
    }
}
