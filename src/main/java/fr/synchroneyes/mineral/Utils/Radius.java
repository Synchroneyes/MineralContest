package fr.synchroneyes.mineral.Utils;

import org.bukkit.Location;

public class Radius {

    public static boolean isBlockInRadius(Location source, Location blockToFind, int radius) {
        return (
                (
                        (
                                source.getX() - radius < blockToFind.getX() &&
                                        source.getX() + radius > blockToFind.getX()
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

    public static boolean isBlockInRadiusWithDividedYAxis(Location source, Location blockToFind, int radius, int divider) {

        return (
                (
                        (
                                source.getX() - radius < blockToFind.getX() &&
                                        source.getX() + radius > blockToFind.getX()
                        ) &&
                                (
                                        source.getZ() - radius < blockToFind.getZ() &&
                                                source.getZ() + radius > blockToFind.getZ()
                                ) &&
                                (
                                        source.getY() - (int) (radius / divider) < blockToFind.getY() &&
                                                source.getY() + (int) (radius / divider) > blockToFind.getY()
                                )
                )
        );

    }
}
