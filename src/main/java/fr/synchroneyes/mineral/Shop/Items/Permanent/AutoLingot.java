package fr.synchroneyes.mineral.Shop.Items.Permanent;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;

public class AutoLingot extends PermanentItem {


    @Override
    public String getNomItem() {
        return Lang.shopitem_auto_ingots_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_auto_ingots_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.BLAST_FURNACE;
    }

    @Override
    public void onItemUse() {

    }

    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("permanent_auto_cook_ore");

    }


}
