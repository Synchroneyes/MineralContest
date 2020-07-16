package fr.synchroneyes.mineral.Shop.Items.Items;

import fr.synchroneyes.mineral.Shop.Items.Abstract.ConsumableItem;
import fr.synchroneyes.mineral.Translation.Lang;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BatonKnockback extends ConsumableItem {
    @Override
    public String getNomItem() {
        return "%red%Matraque";
    }

    @Override
    public String[] getDescriptionItem() {
        return new String[]{"Vous vous sentez aggressé et vous souhaitez vous défendre ?", "Ce baton est fait pour vous !"};
    }

    @Override
    public Material getItemMaterial() {
        return Material.STICK;
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
        ItemStack baton = new ItemStack(Material.STICK);
        ItemMeta meta = baton.getItemMeta();

        meta.setDisplayName(Lang.translate(getNomItem()));
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        baton.setItemMeta(meta);


        joueur.getInventory().addItem(baton);
    }

    @Override
    public int getPrice() {
        return 10;
    }


}
