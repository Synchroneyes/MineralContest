package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Utils.Potion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public abstract class PotionItem extends ShopItem {

    @Override
    public ItemStack toItemStack() {
        return Potion.createPotion(getPotionType(), getPotionLevel(), getPotionDuration(), getNomItem());
    }

    @Override
    public void onPlayerBonusAdded() {
        onItemUse();
    }

    /**
     * Définir le type de potion
     *
     * @return
     */
    public abstract PotionEffectType getPotionType();

    /**
     * Définir le niveau de la potion
     *
     * @return
     */
    public abstract int getPotionLevel();

    /**
     * Définir la durée en minute de la potion
     *
     * @return
     */
    public abstract int getPotionDuration();


    public Material getItemMaterial() {
        return Material.POTION;
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
        joueur.getInventory().addItem(toItemStack());
    }

    @Override
    public String getPurchaseText() {
        return "Vous avez acheté une potion: " + getNomItem();
    }

}
