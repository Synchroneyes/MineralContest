package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Shop.Items.Abstract.GameItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PommeDoree extends GameItem {
    @Override
    public String getNomItem() {
        return "Pomme doré";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous donne une pomme doré"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public void onItemUse() {
        this.joueur.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
    }

    @Override
    public int getPrice() {
        return 10;
    }

}
