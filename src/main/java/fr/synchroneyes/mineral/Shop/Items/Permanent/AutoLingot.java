package fr.synchroneyes.mineral.Shop.Items.Permanent;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import org.bukkit.Material;

public class AutoLingot extends PermanentItem {


    @Override
    public String getNomItem() {
        return "Auto Fourneau";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Permet de dropper des items cuits instantanément"};
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
        return 1500;
    }


}
