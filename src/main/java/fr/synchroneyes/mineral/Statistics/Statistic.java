package fr.synchroneyes.mineral.Statistics;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cette classe permet de gérer les statistiques d'une partie
 */
public abstract class Statistic {


    /**
     * Tableau "associatif" permettant de stocker la valeur d'un joueur ainsi que sa valeur
     */

    public abstract void perform(Player p, Object target);


    /**
     * Méthode permettant de retourner le meilleur joueur de la statistique
     *
     * @return meilleur joueur
     */
    public abstract Player getHighestPlayer();

    /**
     * Méthode permettant de retourner le pire joueur de la statistique
     *
     * @return pire joueur
     */
    public abstract Player getLowestPlayer();

    /**
     * Récuperer les titres des "grades"
     *
     * @return titre
     */
    public abstract String getHighestPlayerTitle();

    public abstract String getLowerPlayerTitle();

    public abstract String getHighestItemSubTitle();

    public abstract String getLowestItemSubTitle();


    /**
     * Récuperer les icones des stats
     *
     * @return icone
     */
    public abstract Material getHighestPlayerIcon();

    public abstract Material getLowestPlayerIcon();


    public abstract int getHighestPlayerValue();

    public abstract int getLowerPlayerValue();

    public abstract boolean isLowestValueRequired();


    /**
     * Retourne si la statistique est utilisable (ex: StatKill, il faut qu'il y ai eu au moins un kill/suicide)
     *
     * @return
     */
    public abstract boolean isStatUsable();


    /**
     * Transforme l'event en un item
     *
     * @return item
     */
    private ItemStack getLowestItemStack() {
        ItemStack item = new ItemStack(getLowestPlayerIcon());
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(getLowerPlayerTitle());
            List<String> description = new LinkedList<>();
            description.add("> " + getLowestPlayer().getDisplayName());
            if (getLowestItemSubTitle() != null) description.add(getLowestItemSubTitle());


            itemMeta.setLore(description);

            item.setItemMeta(itemMeta);
        }

        return item;
    }

    /**
     * Transforme l'event en un item
     *
     * @return item
     */
    private ItemStack getHighestItemStack() {
        ItemStack item = new ItemStack(getHighestPlayerIcon());
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(getHighestPlayerTitle());
            List<String> description = new LinkedList<>();
            description.add("> " + getHighestPlayer().getDisplayName());

            if (getHighestItemSubTitle() != null) description.add(getHighestItemSubTitle());
            itemMeta.setLore(description);

            item.setItemMeta(itemMeta);
        }
        return item;
    }


    /**
     * Transforme la statistique en items
     *
     * @return
     */
    public List<ItemStack> toItemStack() {
        List<ItemStack> items = new ArrayList<>();

        if (!isStatUsable()) return items;


        // On ajoute le moins bon des stats SI le moins bon n'est pas le même que le meilleur!
        if (isLowestValueRequired() && !getLowestPlayer().equals(getHighestPlayer())) items.add(getLowestItemStack());
        items.add(getHighestItemStack());

        return items;
    }


}
