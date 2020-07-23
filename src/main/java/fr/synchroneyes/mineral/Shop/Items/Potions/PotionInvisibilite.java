package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.potion.PotionEffectType;

public class PotionInvisibilite extends PotionItem {

    @Override
    public PotionEffectType getPotionType() {
        return PotionEffectType.INVISIBILITY;
    }

    @Override
    public int getPotionLevel() {
        return 1;
    }

    @Override
    public int getPotionDuration() {
        return 2;
    }


    @Override
    public String getNomItem() {
        return Lang.shopitem_invisibility_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_invisibility_desc.toString()};
    }


    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("invisibility_potion");
    }

}
