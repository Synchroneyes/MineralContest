package fr.mineral;

import fr.mineral.Exception.MaterialNotInRangeException;
import org.bukkit.Material;

public class Range {
    int min, max;
    Material nom;

    public Range(Material nom, int min, int max) {
        this.nom = nom;
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(int valeur) {
        return (min <= valeur && valeur < max);
    }
    public static Material getInsideRange(Range[] r, int valeur) throws MaterialNotInRangeException {
        for(Range interval : r) {
            if(interval.isInRange(valeur))
                return interval.nom;
        }

        throw new MaterialNotInRangeException();
    }
}
