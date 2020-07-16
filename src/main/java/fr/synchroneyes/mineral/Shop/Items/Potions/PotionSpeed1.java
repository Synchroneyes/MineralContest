package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
import org.bukkit.potion.PotionEffectType;

public class PotionSpeed1 extends PotionItem {
    @Override
    public String getNomItem() {
        return "Speed I";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous donne la potion Vitesse I"};
    }

    @Override
    public int getPrice() {
        return 250;
    }


    @Override
    public PotionEffectType getPotionType() {
        return PotionEffectType.SPEED;
    }

    @Override
    public int getPotionLevel() {
        return 1;
    }

    @Override
    public int getPotionDuration() {
        return 5;
    }
}
