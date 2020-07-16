package fr.synchroneyes.mineral.Shop.Items.Abstract;

import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.Potion;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.List;

public abstract class PotionItem extends ShopItem {

    @Override
    public ItemStack toItemStack() {

        ItemStack potion = Potion.createPotion(getPotionType(), getPotionLevel(), getPotionDuration(), getNomItem());
        ItemMeta potionMeta = potion.getItemMeta();

        List<String> description = new LinkedList<>();

        for (String ligne : getDescriptionItem())
            description.add(ChatColor.RESET + Lang.translate(ligne));

        description.add("Price: " + getPrice() + " " + "points");
        potionMeta.setLore(description);

        potion.setItemMeta(potionMeta);

        return potion;
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

        ItemStack potion = toItemStack();

        ItemMeta meta = potion.getItemMeta();
        meta.setLore(null);

        potion.setItemMeta(meta);


        joueur.getInventory().addItem(potion);
    }

    @Override
    public String getPurchaseText() {
        return "Vous avez acheté une potion: " + getNomItem();
    }

    @Override
    public boolean isEnabledOnDeathByAnotherPlayer() {
        return false;
    }

    @Override
    public boolean isEnabledOnDeath() {
        return false;
    }

}
