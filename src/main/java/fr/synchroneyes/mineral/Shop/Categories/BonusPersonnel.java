package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class BonusPersonnel extends Category {
    @Override
    public String getNomCategorie() {
        return "Bonus personnel";
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_MUSHROOM;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez des bonus personnel", "Ces bonus vous donneront un avantage conséquent!"};
    }
}
