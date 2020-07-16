package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import org.bukkit.Material;

public class PotionExperience extends ConsumableItem {


    @Override
    public String getNomItem() {
        return "Experience";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous permet d'acheter un %gold%niveau d'expérience"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.EXPERIENCE_BOTTLE;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return false;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }

    @Override
    public int getNombreUtilisations() {
        return 1;
    }

    @Override
    public void onItemUse() {
        int nombre_level = 1;
        joueur.setLevel(joueur.getLevel() + nombre_level);
    }

    @Override
    public int getPrice() {
        return 5;
    }

}
