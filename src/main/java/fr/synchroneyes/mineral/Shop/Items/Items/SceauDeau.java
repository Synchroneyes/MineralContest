package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SceauDeau extends ConsumableItem {
    @Override
    public String getNomItem() {
        return Lang.shopitem_waterbucket_title.toString();
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{Lang.shopitem_waterbucket_desc.toString()};
    }

    @Override
    public Material getItemMaterial() {
        return Material.WATER_BUCKET;
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
        ItemStack item = new ItemStack(getItemMaterial());

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Lang.translate(getNomItem()));

        item.setItemMeta(meta);

        joueur.getInventory().addItem(item);
    }

    @Override
    public int getPrice() {
        return 50;
    }

}
