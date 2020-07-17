package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Shop.Items.Abstract.GameItem;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;

public class Buche extends GameItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_oak_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_oak_desc.toString()};
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
