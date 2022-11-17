package fr.synchroneyes.mineral.Utils;

import org.bukkit.Material;

public class RawToCooked {

    /**
     * Converti un block en lingot
     *
     * @param material
     * @return
     */
    public static Material toCooked(Material material) {
        switch (material) {
            case GOLD_ORE:
            case RAW_GOLD:
            case DEEPSLATE_GOLD_ORE:
                return Material.GOLD_INGOT;
            case IRON_ORE:
            case RAW_IRON:
            case DEEPSLATE_IRON_ORE:
                return Material.IRON_INGOT;
            case BEEF:
                return Material.COOKED_BEEF;
            case CHICKEN:
                return Material.COOKED_CHICKEN;
            case COD:
                return Material.COOKED_COD;
            case MUTTON:
                return Material.COOKED_MUTTON;
            case PORKCHOP:
                return Material.COOKED_PORKCHOP;
            case RABBIT:
                return Material.COOKED_RABBIT;
            case SALMON:
                return Material.COOKED_SALMON;
            case RAW_COPPER:
                return Material.COPPER_INGOT;
            case DEEPSLATE_DIAMOND_ORE:
                return Material.DIAMOND;
            case DEEPSLATE_EMERALD_ORE:
                return Material.EMERALD;
            default:
                return null;
        }
    }
}
