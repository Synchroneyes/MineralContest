package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.BlockManager;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Shop.Items.Permanent.AutoLingot;
import fr.synchroneyes.mineral.Shop.Players.PlayerBonus;
import fr.synchroneyes.mineral.Statistics.Class.MinerStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.BlockSaver;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.Utils.RawToCooked;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockDestroyed implements Listener {

    /**
     * Evenement appelé lors de la destruction d'un bloc
     *
     * @param event
     */
    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {

        // Si on est en mode création de map, on ignore l'event
        if (MapBuilder.getInstance().isBuilderModeEnabled) {
            return;
        }


        // On récupère le joueur ayant cassé le bloc
        Player joueur = event.getPlayer();

        // Si le joueur n'est pas dans un monde mineral contest, on s'en fou
        if (!mineralcontest.isInAMineralContestWorld(joueur)) return;

        // Le joueur se trouve dans un monde mineral contest
        // On récupère son groupe
        Groupe playerGroupe = mineralcontest.getPlayerGroupe(joueur);

        // Si le group est null, on annule l'event.
        // ça veut dire que le joueur est dans le lobby & version communautaire activée
        if (playerGroupe == null) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
            return;
        }

        // on récupère la partie du joueur
        Game partie = playerGroupe.getGame();

        // Si le joueur est un arbitre, on l'autorise à poser le bloc
        if (partie.isReferee(joueur)) return;

        // Si la partie est non démarrée, on annule l'event, on ne veut pas casser de bloc
        if (!partie.isGameStarted() || partie.isPreGame() || partie.isGamePaused()) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
            return;
        }

        // Maintenant, on doit vérifier si la partie est en cours
        if (partie.isGameStarted()) {


            // On vérifie si c'est un block que l'on veut supprimer sans check


            // On doit vérifier si on se trouve autour de la zone protegée
            int rayonZoneProtege = playerGroupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();
            Block blockDetruit = event.getBlock();
            Location centreArene = playerGroupe.getGame().getArene().getCoffre().getLocation();

            // Si le block détruit est dans le rayon de la zone protegé, on annule l'event
            if (Radius.isBlockInRadius(centreArene, blockDetruit.getLocation(), rayonZoneProtege)) {

                if (canBlockBeDestroyed(event.getBlock())) {
                    event.setDropItems(false);
                    return;
                }

                // Si le block est un bloc ajouté par un joueur, on ne fait rien
                BlockManager blockManager = BlockManager.getInstance();

                if (!blockManager.wasBlockAdded(blockDetruit)) {
                    event.setCancelled(true);
                    joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                    return;
                }


            }

            // Si c'est un block
            if (PlayerBonus.getPlayerBonus(AutoLingot.class, joueur) != null && event.isDropItems()) {
                Material materialToDrop = RawToCooked.toCooked(blockDetruit.getType());

                if (materialToDrop != null) {
                    blockDetruit.setType(Material.AIR);
                    blockDetruit.getWorld().dropItemNaturally(blockDetruit.getLocation(), new ItemStack(materialToDrop));
                }

            }


            Bukkit.getLogger().info(blockDetruit.getState().toString() + " - " + blockDetruit.getState().getClass().getName());
            if (blockDetruit.getState() instanceof InventoryHolder && !partie.isThisBlockAGameChest(blockDetruit)) {
                ((InventoryHolder) blockDetruit.getState()).getInventory().clear();
                event.setDropItems(false);
            }

            // Sinon, le block détruit n'est pas dans la zone protégé, on autorise la destruction
            playerGroupe.getGame().addBlock(event.getBlock(), BlockSaver.Type.DESTROYED);

            // On enregistre la destruction pour les stats
            playerGroupe.getGame().getStatsManager().register(MinerStat.class, event.getPlayer(), null);

        } else {
            // La partie n'est pas démarrée, on annule l'event
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
        }


    }


    @EventHandler
    public void onVehicleDestroyed(VehicleDestroyEvent event) {

        Bukkit.getLogger().info(event.getVehicle().getType().toString());
        if (event.getVehicle().getType() == EntityType.MINECART_CHEST) {
            StorageMinecart vehicle = (StorageMinecart) event.getVehicle();

            if (mineralcontest.isAMineralContestWorld(vehicle.getWorld())) {
                vehicle.getInventory().clear();
            }

        }
    }

    private boolean canBlockBeDestroyed(Block b) {

        List<Material> allowedToBeDestroyed = new ArrayList<>();

        allowedToBeDestroyed.add(Material.GRASS);
        allowedToBeDestroyed.add(Material.WHEAT);
        allowedToBeDestroyed.add(Material.TALL_GRASS);
        allowedToBeDestroyed.add(Material.CHORUS_PLANT);
        allowedToBeDestroyed.add(Material.KELP_PLANT);
        allowedToBeDestroyed.add(Material.BROWN_MUSHROOM);
        allowedToBeDestroyed.add(Material.POTTED_BROWN_MUSHROOM);
        allowedToBeDestroyed.add(Material.RED_MUSHROOM_BLOCK);
        allowedToBeDestroyed.add(Material.RED_MUSHROOM);
        allowedToBeDestroyed.add(Material.MUSHROOM_STEW);
        allowedToBeDestroyed.add(Material.SUGAR_CANE);
        allowedToBeDestroyed.add(Material.FERN);
        allowedToBeDestroyed.add(Material.LARGE_FERN);
        allowedToBeDestroyed.add(Material.POTTED_FERN);
        allowedToBeDestroyed.add(Material.DEAD_BUSH);
        allowedToBeDestroyed.add(Material.POTTED_DEAD_BUSH);
        allowedToBeDestroyed.add(Material.ROSE_BUSH);
        allowedToBeDestroyed.add(Material.SWEET_BERRY_BUSH);
        allowedToBeDestroyed.add(Material.VINE);
        allowedToBeDestroyed.add(Material.SUNFLOWER);
        allowedToBeDestroyed.add(Material.CORNFLOWER);
        allowedToBeDestroyed.add(Material.POTTED_CORNFLOWER);
        allowedToBeDestroyed.add(Material.BEETROOT);
        allowedToBeDestroyed.add(Material.BEETROOTS);
        allowedToBeDestroyed.add(Material.SUNFLOWER);
        allowedToBeDestroyed.add(Material.OXEYE_DAISY);
        allowedToBeDestroyed.add(Material.POTTED_OXEYE_DAISY);


        return allowedToBeDestroyed.contains(b.getType());
    }


}
