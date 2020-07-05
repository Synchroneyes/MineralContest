package fr.synchroneyes.mineral.Core.Coffre.Event;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Coffre.AutomatedChestAnimation;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class ChestOpenEvent implements Listener {

    /**
     * Evenement appelé lors de l'ouverture d'un coffre, cet évènement sert à gérer l'ouverture des coffres d'animations
     *
     * @param event
     */
    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;

            // On applique cet évenement uniquement aux joueurs du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {


                // On récupère la location du bloc contenant cet inventaire
                Location chestLocation = event.getInventory().getLocation();
                if (chestLocation == null) return;

                Block chest = chestLocation.getBlock();


                // Si le coffre ouvert fait parti des blocs d'animation
                if (playerGroup.getAutomatedChestManager().isThisBlockAChestAnimation(chest)) {



                    event.setCancelled(true);
                    // On récupère son instance
                    AutomatedChestAnimation automatedChestAnimation = playerGroup.getAutomatedChestManager().getChestAnomation(chest);


                    if (automatedChestAnimation == null) return;

                    if (automatedChestAnimation.isBeingOpened()) return;

                    // Et on joue l'animation
                    automatedChestAnimation.setOpeningPlayer(joueur);

                }
            }
        }
    }

    /**
     * Evenement appelé lors de la fermeture d'un coffre
     *
     * @param event
     */
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {

        if (event.getPlayer() instanceof Player) {
            Player joueur = (Player) event.getPlayer();
            Groupe playerGroup = mineralcontest.getPlayerGroupe(joueur);

            if (playerGroup == null) return;

            // On applique cet évenement uniquement aux joueurs du plugin
            if (mineralcontest.isInAMineralContestWorld(joueur)) {

                // ON récupère l'inventaire fermé
                Inventory openedInventory = event.getInventory();
                if (playerGroup.getAutomatedChestManager().isThisAnAnimatedInventory(openedInventory)) {
                    // C'est un inventaire avec animation
                    AutomatedChestAnimation automatedChestAnimation = playerGroup.getAutomatedChestManager().getFromInventory(openedInventory);
                    if (automatedChestAnimation == null) return;

                    // Et on ferme l'inventaire
                    automatedChestAnimation.closeInventory();
                }
            }


        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //return;
        Bukkit.getLogger().info("sequence.add(" + event.getSlot() + ");");
    }
}
