package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class BonusPersonnel extends Category {

    public BonusPersonnel(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Bonus personnel";
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_MUSHROOM;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez des bonus personnel", "Ces bonus vous donneront un avantage conséquent!"};
    }
}
