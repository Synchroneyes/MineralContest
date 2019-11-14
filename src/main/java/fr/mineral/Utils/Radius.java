package fr.mineral.Utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Radius {

    public static boolean isBlockInRadius(Location source, Location blockToFind, int radius) {

        return(
                (
                        (
                            source.getX()-radius < blockToFind.getX() &&
                            source.getX()+radius > blockToFind.getX()
                        ) &&
                        (
                            source.getZ() - radius < blockToFind.getZ() &&
                            source.getZ() + radius > blockToFind.getZ()
                         ) &&
                         (
                             source.getY() - radius < blockToFind.getY() &&
                             source.getY() + radius > blockToFind.getY()
                         )
                )
        );

    }
}
