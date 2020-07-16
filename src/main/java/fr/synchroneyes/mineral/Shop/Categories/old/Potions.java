package fr.synchroneyes.mineral.Shop.Categories.old;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class Potions extends Category {

    public Potions(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Potions";
    }

    @Override
    public Material getItemMaterial() {
        return Material.POTION;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez des potions vous donnant un avantage personnel"};
    }
}
