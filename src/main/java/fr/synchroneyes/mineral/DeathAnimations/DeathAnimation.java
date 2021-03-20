package fr.synchroneyes.mineral.DeathAnimations;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;

/**
 * Classe permettant de modéliser des animations de mort
 * Date de création: 21.10.2020
 * Auteur: Synchroneyes
 */

public abstract class DeathAnimation {


    /**
     * Méthode retournant le nom de l'animation
     * @return
     */
    public abstract String getAnimationName();

    public abstract Material getIcone();

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getIcone());

        // Suppression de la description, et mise en place d'un nom custom
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(getAnimationName());
        itemMeta.setLore(new LinkedList<>());
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Méthode permettant de lire l'animation
     * @param player - Joueur devant recevoir l'animation
     */
    public abstract void playAnimation(LivingEntity player);


}
