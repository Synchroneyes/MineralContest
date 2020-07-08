package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class Informations extends Category {
    @Override
    public String getNomCategorie() {
        return "Informations";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez une information", "et dominez la partie !"};
    }
}
