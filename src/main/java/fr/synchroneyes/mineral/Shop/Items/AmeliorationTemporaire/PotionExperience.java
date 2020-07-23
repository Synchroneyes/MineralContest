package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;

public class PotionExperience extends ConsumableItem {


    @Override
    public String getNomItem() {
        return Lang.shopitem_experience_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_experience_desc.toString()};
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
        return ShopManager.getBonusPriceFromName("self_experience");

    }

}
