package fr.synchroneyes.mineral.Core.Arena.ArenaChestContent;

import org.bukkit.Material;

public class ArenaChestItem {
    private Material itemMaterial;
    private int itemProbability;


    public Material getItemMaterial() {
        return itemMaterial;
    }

    public void setItemMaterial(String itemMaterial) {
        this.itemMaterial = Material.valueOf(itemMaterial);
    }

    public int getItemProbability() {
        return itemProbability;
    }

    public void setItemProbability(int itemProbability) {
        this.itemProbability = itemProbability;
    }

}
