package fr.synchroneyes.mineral.Shop.Categories.old;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class BonusNiveaux extends Category {


    public BonusNiveaux(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Amélioration avec niveau";
    }

    @Override
    public Material getItemMaterial() {
        return Material.EMERALD;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}
