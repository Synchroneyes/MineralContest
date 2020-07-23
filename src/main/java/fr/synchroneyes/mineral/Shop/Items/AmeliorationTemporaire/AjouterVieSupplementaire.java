package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public class AjouterVieSupplementaire extends ConsumableItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_temp_max_health_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_temp_max_health_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.RED_MUSHROOM;
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

        int nombreDeCoeur = 3;

        double currentMaxHealth = this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        currentMaxHealth += nombreDeCoeur * 2;

        if (currentMaxHealth >= 40) currentMaxHealth = 40;

        this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth);
        this.joueur.setHealth(currentMaxHealth);
    }

    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("self_more_health");

    }

}
