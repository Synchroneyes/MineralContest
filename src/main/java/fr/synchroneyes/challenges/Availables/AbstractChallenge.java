package fr.synchroneyes.challenges.Availables;


import fr.synchroneyes.challenges.ChallengeManager;
import fr.synchroneyes.challenges.Rewards.AbstractReward;
import fr.synchroneyes.mineral.Core.MCPlayer;
import org.bukkit.event.Listener;

/**
 * Classe représentant un succès à faire
 */
public abstract class AbstractChallenge implements Listener {

    private MCPlayer player;
    private ChallengeManager manager;

    public AbstractChallenge(ChallengeManager manager) {
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

    public ChallengeManager getManager() {
        return manager;
    }


}
