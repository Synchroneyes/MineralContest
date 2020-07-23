package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
import fr.synchroneyes.mineral.Shop.ShopManager;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.potion.PotionEffectType;

public class PotionHaste extends PotionItem {
    @Override
    public PotionEffectType getPotionType() {
        return PotionEffectType.FAST_DIGGING;
    }

    @Override
    public int getPotionLevel() {
        return 1;
    }

    @Override
    public int getPotionDuration() {
        return 10;
    }

    @Override
    public String getNomItem() {
        return Lang.shopitem_haste_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_haste_desc.toString()};
    }

    @Override
    public int getPrice() {
        return ShopManager.getBonusPriceFromName("haste_potion");
    }


}
