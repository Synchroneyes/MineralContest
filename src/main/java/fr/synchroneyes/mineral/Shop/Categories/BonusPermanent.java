package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;

public class BonusPermanent extends Category {
    public BonusPermanent(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return Lang.permanent_category_title.toString();
    }

    @Override
    public Material getItemMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}
