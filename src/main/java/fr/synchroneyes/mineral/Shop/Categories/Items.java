package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class Items extends Category {

    public Items(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Items";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BRICKS;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetes items"};
    }
}
