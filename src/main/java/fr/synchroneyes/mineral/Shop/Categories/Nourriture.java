package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class Nourriture extends Category {
    @Override
    public String getNomCategorie() {
        return "Nourriture";
    }

    @Override
    public Material getItemMaterial() {
        return Material.APPLE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez de la nourriture"};
    }
}
