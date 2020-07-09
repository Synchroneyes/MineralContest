package fr.synchroneyes.mineral.Shop.Items.Abstract;

import org.bukkit.inventory.ItemStack;

public abstract class GameItem extends ConsumableItem {

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
        this.joueur.getInventory().addItem(new ItemStack(getItemMaterial()));

    }
}
