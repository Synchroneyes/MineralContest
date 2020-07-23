package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Utils.RawToCooked;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Cuit instantanément ses mienrais, mais retire une ligne dans l'inventaire
 */
public class Mineur extends KitAbstract {
    @Override
    public String getNom() {
        return "Mineur";
    }

    @Override
    public String getDescription() {
        return "Cuit instantanément vos minerais, mais vous retire une ligne dans votre inventaire";
    }


    /**
     * Evenement appelé lors de l'ajout du kit par un joueur
     *
     * @param event
     */
    @EventHandler
    public void onKitSelected(PlayerKitSelectedEvent event) {
        // Si le joueur n'utilise pas ce kit, on s'arrête
        if (!isPlayerUsingThisKit(event.getPlayer())) return;

        Player joueur = event.getPlayer();
        Groupe groupe = mineralcontest.getPlayerGroupe(joueur);

        // On sauvegarde son inventaire pour pouvoir lui remettre après
        //ItemStack[] items = joueur.getInventory().getStorageContents();
        //ItemStack[] armure = joueur.getInventory().getArmorContents();

        // On vide son inventaire
        joueur.getInventory().clear();

        // On lui ajoute les barrières, on block la première ligne
        for (int index = 9; index < 18; ++index)
            joueur.getInventory().setItem(index, getBarrierItem());

        // On lui donne le stuff de base
        groupe.getPlayerBaseItem().giveItemsToPlayer(joueur);


    }


    /**
     * Fonction appelé lors du respawn du joueur
     *
     * @param event
     */
    @EventHandler
    public void onPlayerRespawn(MCPlayerRespawnEvent event) {

        // Si le joueur n'utilise pas ce kit, on s'arrête
        if (!isPlayerUsingThisKit(event.getJoueur())) return;


        // On ajoute les lignes dans l'inventaire du joueur
        for (int i = 9; i < 18; ++i)
            event.getJoueur().getInventory().setItem(i, getBarrierItem());
    }


    /**
     * Fonction appelé lors du clic d'un item dans l'inventaire
     *
     * @param event
     */
    @EventHandler
    public void onInventoryItemClick(InventoryClickEvent event) {
        // Si le joueur n'utilise pas ce kit, on s'arrête
        if (!isPlayerUsingThisKit((Player) event.getWhoClicked())) return;

        if (event.getCursor() != null) {
            ItemStack clickedItem = event.getCurrentItem();

            if (event.getWhoClicked() instanceof Player) {
                Player joueur = (Player) event.getWhoClicked();
                if (event.getClickedInventory() != null && event.getClickedInventory().equals(joueur.getInventory())) {
                    if (clickedItem != null && clickedItem.equals(getBarrierItem())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    /**
     * Fonction appelé lors de la destruction d'un bloc
     *
     * @param event
     */
    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {

        // Si le joueur n'utilise pas ce kit, on s'arrête
        if (!isPlayerUsingThisKit((Player) event.getPlayer())) return;

        Block blockDetruit = event.getBlock();
        Material materialToDrop = RawToCooked.toCooked(blockDetruit.getType());

        if (materialToDrop != null) {
            blockDetruit.setType(Material.AIR);
            blockDetruit.getWorld().dropItemNaturally(blockDetruit.getLocation(), new ItemStack(materialToDrop));
        }
    }


    /**
     * Permet de retourner l'item symbolisant une case occupée
     *
     * @return
     */
    private ItemStack getBarrierItem() {
        ItemStack barriere = new ItemStack(Material.BARRIER);

        ItemMeta meta = barriere.getItemMeta();
        meta.setDisplayName("Accès interdit");

        barriere.setItemMeta(meta);
        return barriere;
    }
}
