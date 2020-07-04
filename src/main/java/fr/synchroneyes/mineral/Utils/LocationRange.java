package fr.synchroneyes.mineral.Utils;

import org.bukkit.Location;

public class LocationRange {

    /**
     * Fonction permettant de vérifier si une range donnée est comprise entre 2 loc
     *
     * @param check
     * @param min
     * @param max
     * @return
     */
    public static boolean isLocationBetween(Location check, Location target, Location min, Location max) {
        return (min.getX() < check.getX() && check.getX() < max.getX() &&
                min.getY() < check.getY() && check.getY() < max.getY() &&
                min.getZ() < check.getZ() && check.getZ() < max.getZ());
    }


    public static boolean isLocationBetween(Location check, Location target, int min, int max) {
        Location _min = target.clone();
        Location _max = target.clone();

        _min.setX(_min.getX() - min);
        _min.setY(_min.getY() - min);
        _min.setZ(_min.getZ() - min);

        _max.setX(_max.getX() + min);
        _max.setY(_max.getY() + min);
        _max.setZ(_max.getZ() + min);

        return isLocationBetween(check, target, _min, _max);
    }

}
