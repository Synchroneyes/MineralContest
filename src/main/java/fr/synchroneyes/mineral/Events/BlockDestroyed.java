package fr.synchroneyes.mineral.Events;

import fr.synchroneyes.groups.Core.Groupe;
import fr.synchroneyes.mapbuilder.MapBuilder;
import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Statistics.Class.MinerStat;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.BlockSaver;
import fr.synchroneyes.mineral.Utils.Radius;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BlockDestroyed implements Listener {

    public static LinkedHashMap<Location, Material> blocks = new LinkedHashMap<>();
    public static Location positionCoffre = null;
    public static int index = 0;
    /**
     * Evenement appelé lors de la destruction d'un bloc
     *
     * @param event
     */
    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {

        // Si on est en mode création de map, on ignore l'event
        if (MapBuilder.getInstance().isBuilderModeEnabled) {
            /*if(index == 0) positionCoffre = event.getBlock().getLocation();
            Location substractedLocation = null;

            Location blockLocation = event.getBlock().getLocation();

            substractedLocation = new Location(positionCoffre.getWorld(),
                    blockLocation.getX() - positionCoffre.getX(),
                    blockLocation.getY() - positionCoffre.getY(),
                    blockLocation.getZ() - positionCoffre.getZ()
            );


            try {
                File fichierConfig = new File(mineralcontest.plugin.getDataFolder(), "airdrop.yml");
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(fichierConfig);
                yamlConfiguration.set(index + ".x", substractedLocation.getX());
                yamlConfiguration.set(index + ".y", substractedLocation.getY());
                yamlConfiguration.set(index + ".z", substractedLocation.getZ());
                yamlConfiguration.set(index + ".material", event.getBlock().getType().toString());

                yamlConfiguration.save(fichierConfig);

                ++index;
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
*/


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

        // Si la partie est non démarrée, on annule l'event, on ne veut pas casser de bloc
        if (!partie.isGameStarted() || partie.isPreGame() || partie.isGamePaused()) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
            event.setCancelled(true);
            return;
        }

        // Maintenant, on doit vérifier si la partie est en cours
        if (partie.isGameStarted()) {


            // On vérifie si c'est un block que l'on veut supprimer sans check
            if (canBlockBeDestroyed(event.getBlock())) {
                event.setDropItems(false);
                return;
            }

            // On doit vérifier si on se trouve autour de la zone protegée
            int rayonZoneProtege = playerGroupe.getParametresPartie().getCVAR("protected_zone_area_radius").getValeurNumerique();
            Block blockDetruit = event.getBlock();
            Location centreArene = playerGroupe.getGame().getArene().getCoffre().getPosition();

            // Si le block détruit est dans le rayon de la zone protegé, on annule l'event
            if (Radius.isBlockInRadius(centreArene, blockDetruit.getLocation(), rayonZoneProtege)) {
                event.setCancelled(true);
                joueur.sendMessage(mineralcontest.prefixErreur + Lang.cant_break_block_here.toString());
                return;
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
