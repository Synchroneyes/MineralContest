package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Shop.Items.Abstract.GameItem;
import org.bukkit.Material;

public class Buche extends GameItem {
    @Override
    public String getNomItem() {
        return "Bûche";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Achetez une buche"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.ACACIA_WOOD;
    }


    @Override
    public int getPrice() {
        return 1;
    }

    @Override
    public Material getCurrency() {
        return Material.IRON_INGOT;
    }
}
