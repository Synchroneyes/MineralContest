package fr.synchroneyes.mineral.Shop.Items.Permanent;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PermanentItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public class AjoutCoeursPermanent extends PermanentItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_more_health_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_more_health_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.CAKE;
    }

    @Override
    public void onItemUse() {
        int nombreDeCoeur = 5;

        double currentMaxHealth = this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        currentMaxHealth += nombreDeCoeur * 2;

        if (currentMaxHealth >= 2048) currentMaxHealth = 2048;

        this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth);
        this.joueur.setHealth(currentMaxHealth);
    }

    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("permanent_more_health");

    }

    @Override
    public boolean isEnabledOnReconnect() {
        return false;
    }
}
