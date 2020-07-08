package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class Potions extends Category {
    @Override
    public String getNomCategorie() {
        return "Potions";
    }

    @Override
    public Material getItemMaterial() {
        return Material.POTION;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez des potions vous donnant un avantage personnel"};
    }
}
