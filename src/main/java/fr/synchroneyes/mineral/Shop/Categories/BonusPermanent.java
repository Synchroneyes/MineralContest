package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class BonusPermanent extends Category {
    @Override
    public String getNomCategorie() {
        return "Bonus permanent";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BONE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Un bonus permanent est un bonus", "que vous conservez même à votre mort"};
    }
}
