package fr.synchroneyes.mineral.Core.Parachute;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe permettant de définir un parachute
 */
public class Parachute {


    // Blocs représentant le parachute
    private Map<String, ParachuteBlock> blocksParachute;

    // Santé du parachute
    private double health;

    // Si vrai, alors le parachute tombe à grande vitesse
    private boolean isFalling = false;

    private boolean isChestOpened = false;

    // Vitesse de chute de plannage (jsp si ça se dit comme ça)
    private int normalFallingSpeed = 20;

    // Vitesse en chute libre
    private int freeFallingSpeed = 5;

    // Vitesse de chute du parachute
    private int currentFallingSpeed = normalFallingSpeed;


    /**
     * Constructeur, prend en paramètre la santé que doit avoir le parachute
     *
     * @param health - santé du parachute
     */
    public Parachute(double health) {
        this.health = health;
        this.blocksParachute = new LinkedHashMap<>();
    }


    /**
     * Retourne vrai si le parachute est touché par une flèche
     *
     * @param fleche La flèche tirée
     * @return
     */
    public boolean isParachuteHit(Arrow fleche) {
        return isParachuteHit(fleche.getLocation().getBlock().getLocation());
    }

    /**
     * Retourne vrai si la localisation donnée est une localisation d'un des blocs du parachute
     *
     * @param loc
     * @return
     */
    public boolean isParachuteHit(Location loc) {
        for (Map.Entry<String, ParachuteBlock> blockDeParachute : getParachute().entrySet())
            if (blockDeParachute.getValue().getLocation().equals(loc)) return true;
        return false;
    }

    /**
     * Fonction appelée lorsque le parachute prend des dégats
     *
     * @param damage
     */
    public void receiveDamage(Double damage) {
        if (health <= damage) {
            this.setFalling(true);
            return;
        }

        health -= damage;
    }

    /**
     * Retourne le parachute
     *
     * @return HashMap parachute
     */
    public Map<String, ParachuteBlock> getParachute() {
        return blocksParachute;
    }


    /**
     * Fait descendre tout les blocs de parachute d'une hauteur
     *
     * @param checkUnderBefore - Si vrai, on vérifiera si le bloc du dessous sera de l'air ou non, et si c'est le cas, on casse le parachute
     */
    private void makeParachuteGoDown(boolean checkUnderBefore) {

        // Pour chaque bloc de parachute
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet()) {

            // On récupère le bloc
            ParachuteBlock parachuteBlock = blocks.getValue();
            Block block = parachuteBlock.getLocation().getBlock();

            // On vérifie d'abord si on doit vérifier en dessous ou pas
            if (checkUnderBefore) {

                // On regarde si le bloc en dessous de notre bloc actuel est de l'air ou non
                if (block.getRelative(BlockFace.DOWN, 1).getType() != Material.AIR) {
                    // Le bloc n'est pas de l'air ... On arrête donc la tombée du parachute, et on casse le parachute
                    this.breakParachute();
                    return;
                }
            }

            // Si on arrive ici, c'est soit que l'on ne vérifie pas en dessous, soit que la vérif s'est bien passée et qu'on peut descendre
            // On va donc faire descendre le bloc de 1 de hauteur
            // On initialise un nouveau block, que l'on va remplacer après
            ParachuteBlock nouveauBlock = null;
            Location nouvelleLocation = block.getLocation();
            nouvelleLocation.setY(nouvelleLocation.getY() - 1);

            nouveauBlock = new ParachuteBlock(nouvelleLocation, block.getType());

            // On supprime l'ancien bloc
            parachuteBlock.remove();

            // Et on le remplace
            blocksParachute.replace(blocks.getKey(), nouveauBlock);

        }
    }


    /**
     * Fonction permettant de vérifier si un bloc du parachute touche quelque chose
     *
     * @return
     */
    private boolean isParachuteHittingABlock() {
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet())
            if (blocks.getValue().getLocation().getBlock().getRelative(BlockFace.DOWN, 1).getType() != Material.AIR)
                return true;
        return false;
    }


    /**
     * Fonction permettant de vérifier si le bloc donné fait parti du parachute
     *
     * @param b block
     * @return
     */
    private boolean isThisBlockAParachute(Block b) {
        for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet())
            if (blocks.getValue().getLocation().equals(b.getLocation())) return true;
        return false;
    }


    /**
     * Si le parachute est marqué comme tombant, alors tout sera retiré du parachute sauf le coffre qui lui, continuera de tomber
     *
     * @param falling
     * @return
     */
    private void setFalling(boolean falling) {

        this.isFalling = falling;

        // Si on marque le parachute comme tombant
        if (falling) {

            // On regarde chaque bloc du parachute; et ceux qui ne sont pas des coffres, on les laisse.
            // Sinon, on les marque comme étant de l'air (donc on les supprime) et on les retire de la hashmap
            for (Map.Entry<String, ParachuteBlock> blocks : blocksParachute.entrySet()) {

                // On récupère le bloc
                ParachuteBlock block = blocks.getValue();

                // Si le bloc n'est pas le coffre, on le retire
                if (block.getMaterial() != Material.CHEST) {
                    block.remove();
                    blocksParachute.remove(blocks.getKey());
                    currentFallingSpeed = freeFallingSpeed;
                }

            }
        }
    }


    /**
     * Permet de casser le parachute et de ne laisser que le coffre
     */
    private void breakParachute() {
        // Pour chaque bloc du parachute
        for (Map.Entry<String, ParachuteBlock> block : blocksParachute.entrySet())
            if (block.getValue().getLocation().getBlock().getType() != Material.CHEST) {
                block.getValue().remove();
                blocksParachute.remove(block.getKey());
            }
    }
}
