package fr.synchroneyes.mineral.Shop.Items.Potions;

import fr.synchroneyes.mineral.Shop.Items.Abstract.PotionItem;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class PotionSpeed2 extends PotionItem {
    @Override
    public String getNomItem() {
        return "Speed II";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous donne la potion Vitesse II"};
    }


    @Override
    public int getPrice() {
        return 15;
    }

    @Override
    public Material getCurrency() {
        return Material.GOLD_INGOT;
    }

    @Override
    public PotionEffectType getPotionType() {
        return PotionEffectType.SPEED;
    }

    @Override
    public int getPotionLevel() {
        return 2;
    }

    @Override
    public int getPotionDuration() {
        return 5;
    }
}
