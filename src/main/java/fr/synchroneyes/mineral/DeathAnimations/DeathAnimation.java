package fr.synchroneyes.mineral.DeathAnimations;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

    /**
     * Méthode permettant de lire l'animation
     * @param player - Joueur devant recevoir l'animation
     */
    public abstract void playAnimation(LivingEntity player);


}
