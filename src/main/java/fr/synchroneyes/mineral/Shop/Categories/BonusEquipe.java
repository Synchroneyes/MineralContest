package fr.synchroneyes.mineral.Shop.Categories;

import org.bukkit.Material;

public class BonusEquipe extends Category {
    @Override
    public String getNomCategorie() {
        return "Bonus équipe";
    }

    @Override
    public Material getItemMaterial() {
        return Material.SHIELD;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez un bonus qui donnera un avantage", "à toute votre équipe !"};
    }
}
