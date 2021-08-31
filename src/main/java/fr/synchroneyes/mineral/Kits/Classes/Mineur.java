package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.custom_events.MCGameStartedEvent;
import fr.synchroneyes.custom_events.MCPlayerRespawnEvent;
import fr.synchroneyes.custom_events.PlayerKitSelectedEvent;
import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.RawToCooked;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

/**
 * Cuit instantanément ses mienrais, mais retire une ligne dans l'inventaire
 */
public class Mineur extends KitAbstract {


    // Le pourcentage de réduction de vitesse du joueur
    private double pourcentageReductionVitesse = 15.0;


    @Override
    public String getNom() {
        return Lang.kit_miner_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_miner_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.GOLDEN_PICKAXE;
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
     * Evenement appelé lors du démarrage de la game
     *
     * @param event
     */
    @EventHandler
    public void onGameStart(MCGameStartedEvent event) {
        Game partie = event.getGame();

        // Pour chaque joueur de la partie
        for (Player joueur : partie.groupe.getPlayers()) {
            // On vérifie si ils ont ce kit
            if (isPlayerUsingThisKit(joueur)) {
                // Et on réduit sa vitesse, et on retire sa vie

                // On lui ajoute les barrières, on block la première ligne
                for (int index = 9; index < 18; ++index)
                    joueur.getInventory().setItem(index, getBarrierItem());


            }
        }
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

        // On applique le ralentissement
        setPlayerEffects(event.getJoueur());
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

            int maxXP = 3;
            int minXP = 1;

            int nombreXpRandom = new Random().nextInt(maxXP - minXP) + 1 + minXP;

            // On donne l'xp du bloc
            event.getPlayer().giveExp(new Random().nextInt(5));

            // On joue le son de l'xp
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 1);

            // On supprime le block
            blockDetruit.setType(Material.AIR);
            blockDetruit.getWorld().dropItemNaturally(blockDetruit.getLocation(), new ItemStack(materialToDrop));
        }
    }


    /**
     * Permet de retourner l'item symbolisant une case occupée
     *
     * @return
     */
    public static ItemStack getBarrierItem() {
        ItemStack barriere = new ItemStack(Material.BARRIER);

        ItemMeta meta = barriere.getItemMeta();
        meta.setDisplayName(Lang.kit_miner_item_denied.toString());

        barriere.setItemMeta(meta);
        return barriere;
    }


    /**
     * Fonction permettant d'ajouter à un joueur, les effets de  ce  kit
     *
     * @param joueur
     */
    private void setPlayerEffects(Player joueur) {

        // On récupère la vitesse de base d'un joueur
        double currentSpeed = 0.2f;


        // On calcule sa nouvelle vitesse
        double newSpeed = currentSpeed - (currentSpeed * pourcentageReductionVitesse / 100);

        joueur.setWalkSpeed((float) newSpeed);

        joueur.setHealth(joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }
}
