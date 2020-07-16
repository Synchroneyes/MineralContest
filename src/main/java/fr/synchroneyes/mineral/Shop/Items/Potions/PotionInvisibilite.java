package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
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
        return "Potion d'invisiblité";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous donne une potion d'invisiblité pendant 2 minutes"};
    }


    @Override
    public int getPrice() {
        return 500;
    }

}
