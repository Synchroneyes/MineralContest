package fr.synchroneyes.mineral.Shop.Categories.old;

import fr.synchroneyes.mineral.Shop.Categories.Abstract.Category;
import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class Informations extends Category {
    public Informations(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Informations";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BOOK;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Achetez une information", "et dominez la partie !"};
    }
}
