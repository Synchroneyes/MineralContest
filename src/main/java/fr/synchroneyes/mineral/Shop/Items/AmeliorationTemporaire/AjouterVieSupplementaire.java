package fr.synchroneyes.mineral.Shop.Items.AmeliorationTemporaire;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;

public class AjouterVieSupplementaire extends ConsumableItem {
    @Override
    public String getNomItem() {
        return "Vie supplémentaire";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Permet d'ajouter 5 coeurs jusqu'à votre mort"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.DRAGON_BREATH;
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

        int nombreDeCoeur = 3;

        double currentMaxHealth = this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        currentMaxHealth += nombreDeCoeur * 2;

        if (currentMaxHealth >= 2048) currentMaxHealth = 2048;

        this.joueur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth);
        this.joueur.setHealth(currentMaxHealth);
    }

    @Override
    public int getPrice() {
        return 3;
    }

    @Override
    public Material getCurrency() {
        return Material.DIAMOND;
    }
}
