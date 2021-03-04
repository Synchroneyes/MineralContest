package fr.synchroneyes.achievements.Availables;


import fr.synchroneyes.achievements.AchievementManager;
import fr.synchroneyes.achievements.Rewards.AbstractReward;
import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * Classe représentant un succès à faire
 */
public abstract class AbstractAchievement implements Listener {

    private MCPlayer player;
    private AchievementManager manager;

    public AbstractAchievement(AchievementManager manager) {
        this.manager = manager;
    }

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


    public void setAchievementCompleted(MCPlayer mcPlayer) {
        AbstractReward abstractReward = getReward();
        abstractReward.setJoueur(mcPlayer.getJoueur());
        abstractReward.rewardPlayer();
    }

    public MCPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MCPlayer player) {
        this.player = player;
    }

    public AchievementManager getManager() {
        return manager;
    }


}
