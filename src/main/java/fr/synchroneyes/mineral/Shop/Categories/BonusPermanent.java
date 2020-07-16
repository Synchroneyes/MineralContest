package fr.synchroneyes.mineral.Shop.Categories;

import fr.synchroneyes.mineral.Shop.NPCs.BonusSeller;
import org.bukkit.Material;

public class BonusPermanent extends Category {

    public BonusPermanent(BonusSeller npc) {
        super(npc);
    }

    @Override
    public String getNomCategorie() {
        return "Bonus permanent";
    }

    @Override
    public Material getItemMaterial() {
        return Material.BONE;
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Un bonus permanent est un bonus", "que vous conservez même à votre mort"};
    }
}
