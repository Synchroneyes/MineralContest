package fr.synchroneyes.mineral.Shop.Items.Abstract;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant un item utilisable (avec un certain nombre d'utilisations)
 */
public abstract class ConsumableItem extends ShopItem {

    @Getter
    @Setter
    private int nombreUtilisationRestantes = getNombreUtilisations();


    public String getPurchaseText() {
        return "Vous avez acheté " + getNombreUtilisations() + "x " + getNomItem();
    }

    public void onPlayerBonusAdded() {
        onItemUse();
    }


}
