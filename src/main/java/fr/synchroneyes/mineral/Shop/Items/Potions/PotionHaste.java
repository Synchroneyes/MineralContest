package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
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
        return "Effet de haste";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Améliore votre vitesse de minagne"};
    }

    @Override
    public int getPrice() {
        return 1;
    }


}
