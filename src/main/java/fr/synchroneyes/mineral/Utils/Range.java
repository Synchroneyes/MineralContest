package fr.synchroneyes.mineral.Utils;

import fr.synchroneyes.mineral.Exception.MaterialNotInRangeException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public class Range {


    int min = Integer.MIN_VALUE;
    int max = Integer.MIN_VALUE;
    Material nom;

    public Range(Material nom, int min, int max) {
        this.nom = nom;
        this.min = min;
        this.max = max;
    }

    public Range() {
    }

    public boolean isFilled() {
        return (nom != null && max != Integer.MIN_VALUE && min != Integer.MIN_VALUE);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Material getMaterial() {
        return nom;
    }

    public void setMaterial(Material nom) {
        this.nom = nom;
    }

    public boolean isInRange(int valeur) {
        return (min <= valeur && valeur < max);
    }
    public static Material getInsideRange(Range[] r, int valeur) throws MaterialNotInRangeException {
        for (Range interval : r) {
            if (interval.isInRange(valeur))
                return interval.nom;
        }

        throw new MaterialNotInRangeException();
    }

    public static ItemStack getRandomItemFromLinkedList(LinkedList<Range> items, int itemNumber) {
        for (Range item : items)
            if (item.isInRange(itemNumber))
                return new ItemStack(item.getMaterial(), 1);
        return null;
    }


}
