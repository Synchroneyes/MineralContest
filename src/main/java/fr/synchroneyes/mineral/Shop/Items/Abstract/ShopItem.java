package fr.synchroneyes.mineral.Shop.Items.Abstract;

import lombok.AccessLevel;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe représentant un item
 */
public abstract class ShopItem {

    private int nombreUtilisationRestante = getNombreUtilisations();

    // Variable contenant le joueur ayant acheté l'item
    @Setter(AccessLevel.PUBLIC)
    protected Player joueur;

    /**
     * Action effectué lorsqu'un bonus est ajouté à un joueur
     */
    public abstract void onPlayerBonusAdded();

    /**
     * Permet de récuperer le nom de l'item
     *
     * @return
     */
    public abstract String getNomItem();

    /**
     * Récupérer la description d'un item
     *
     * @return
     */
    public abstract String[] getDescriptionItem();

    /**
     * Récuperer l'item à utiliser
     *
     * @return
     */
    public abstract Material getItemMaterial();


    /**
     * Permet de convertir un item en ItemStack
     *
     * @return
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemMaterial(), 1);
        if (item.getItemMeta() != null) {
            List<String> description = new LinkedList<>();
            Collections.addAll(description, getDescriptionItem());

            description.add("Price: " + getPrice() + " " + getCurrency().toString());

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(description);
            itemMeta.setDisplayName(getNomItem());

            item.setItemMeta(itemMeta);
        }
        return item;
    }

    /**
     * Permet de savoir si l'item s'active à l'achat, ou au respawn
     *
     * @return
     */
    public abstract boolean isEnabledOnRespawn();

    public abstract boolean isEnabledOnPurchase();

    /**
     * Récupère le nombre d'utilisation d'un item
     *
     * @return
     */
    public abstract int getNombreUtilisations();


    /**
     * Permet d'utiliser un item sur un joueur
     */
    public abstract void onItemUse();

    /**
     * Récupère le texte d'achat
     *
     * @return
     */
    public abstract String getPurchaseText();


    /**
     * Retourne le prix d'un item
     *
     * @return
     */
    public abstract int getPrice();

    /**
     * Retourne le type d'item demandé
     *
     * @return
     */
    public abstract Material getCurrency();

}
