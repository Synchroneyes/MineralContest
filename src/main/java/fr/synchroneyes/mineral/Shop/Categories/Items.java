package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class Items extends Category {
    @Override
    public String getNomCategorie() {
        return "Items";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BRICKS;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetes items"};
    }
}
