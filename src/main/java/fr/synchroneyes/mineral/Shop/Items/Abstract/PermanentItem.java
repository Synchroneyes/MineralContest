package fr.synchroneyes.mineral.Shop.Items.Abstract;

/**
 * Classe permettant de gérer les items permanents
 */
public abstract class PermanentItem extends ShopItem {

    public int getNombreUtilisations() {
        return Integer.MAX_VALUE;
    }

    public int getNombreUtilisationsRestantes() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEnabledOnRespawn() {
        return true;
    }

    @Override
    public boolean isEnabledOnPurchase() {
        return true;
    }


}
