package fr.synchroneyes.mineral.Kits.Classes;

import fr.synchroneyes.mineral.Exception.MaterialNotInRangeException;
import fr.synchroneyes.mineral.Kits.KitAbstract;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Player.PlayerUtils;
import fr.synchroneyes.mineral.Utils.Range;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;


/**
 * Lors qu'il mine de l'emeraude, 20% de chance d'avoir 2 émeraude, 20% de chance d'avoir 2 dirts
 */
public class Parieur extends KitAbstract {


    // Sur quel block on doit réagir
    private Material materialToReact = Material.EMERALD_ORE;

    // Pourcentage de mauvaise chance
    private double badLuckPercentage = 20.0;

    // Pourcentage de bonne chance
    private double goodLuckPercentage = 20.0;

    // Nombre de block a obtenir en cas de mauvaise chance
    private int badLuckMultiplier = 2;

    // Item à donner en cas de mauvaise chance
    private Material badLuckMaterial = Material.DIRT;

    // Nombre de block a obtenir en cas de bonne chance
    private int goodLuckMultiplier = 2;

    // Item à donner en cas de bonne chance
    private Material goodLuckMaterial = Material.EMERALD;


    @Override
    public String getNom() {
        return Lang.kit_crazy_bet_title.toString();
    }

    @Override
    public String getDescription() {
        return Lang.kit_crazy_bet_description.toString();
    }

    @Override
    public Material getRepresentationMaterialForSelectionMenu() {
        return Material.EMERALD_ORE;
    }


    /**
     * Evenement appelé lors de la destruction d'un block
     */
    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) throws MaterialNotInRangeException {

        // Si le joueur n'a pas ce kit, on s'arrête
        if (!isPlayerUsingThisKit(event.getPlayer())) return;

        // Si c'est le bloc où on doit réagir
        if (event.getBlock().getType() == materialToReact) {

            // On vérifie si il doit y avoir un drop
            if (event.isDropItems()) {

                // On calcule la chance du joueur
                Range[] range = new Range[3];
                range[0] = new Range(goodLuckMaterial, 0, (int) goodLuckPercentage);
                range[1] = new Range(Material.AIR, (int) goodLuckPercentage, (int) (100 - badLuckPercentage));
                range[2] = new Range(badLuckMaterial, (int) (100 - badLuckPercentage), 100);

                Random random = new Random();

                // On récupère un nombre aléatoire
                int nombreAleatoire = random.nextInt(100);

                Material materialToDrop = Range.getInsideRange(range, nombreAleatoire);

                // Si il ne s'est rien passé, on est ni gagnant ni perdant, on s'arrête
                if (materialToDrop == Material.AIR) return;

                // Sinon, on retire les drops
                Block blockDestroyed = event.getBlock();
                blockDestroyed.getDrops().clear();

                event.setDropItems(false);


                Player joueur = event.getPlayer();

                // Si le joueur a de la chance
                if (materialToDrop == goodLuckMaterial) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(goodLuckMaterial, goodLuckMultiplier));

                    // Et on joue un son
                    //joueur.playSound(joueur.getLocation(), Sound.ACH, 1,1);
                    PlayerUtils.setFirework(joueur, Color.GREEN, 0);
                    PlayerUtils.setFirework(joueur, Color.RED, 1);
                } else {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(badLuckMaterial, badLuckMultiplier));

                    joueur.playSound(joueur.getLocation(), Sound.ENTITY_PIG_HURT, 1, 1);

                }


            }
        }
    }

}
