package fr.synchroneyes.achievements.Availables;


import fr.synchroneyes.achievements.Rewards.AbstractReward;

/**
 * Classe représentant un succès à faire
 */
public abstract class AbstractAchievement {



    /**
     * Retourne le nom du succès
     * @return
     */
    public abstract String getNom();


    /**
     * Permet de retourner ce qu'il faut faire pour réaliser le succès
     * @return
     */
    public abstract String getObjectifTexte();


    /**
     * Permet de retourner la récompense pour un joueur
     * @return
     */
    public abstract AbstractReward getReward();
}
