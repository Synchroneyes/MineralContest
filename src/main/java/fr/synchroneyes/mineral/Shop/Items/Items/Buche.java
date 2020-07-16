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
        return Material.OAK_LOG;
    }


    @Override
    public int getPrice() {
        return 5;
    }

}
