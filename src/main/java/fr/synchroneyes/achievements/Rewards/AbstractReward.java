package fr.synchroneyes.achievements.Rewards;

import fr.synchroneyes.mineral.Core.MCPlayer;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.entity.Player;

public abstract class AbstractReward {

    private Player joueur;

    private MCPlayer mcPlayer;


    /**
     * Méthode permettant de donner une récompense à un joueur
     */
    public abstract void giveToPlayer();


    /**
     * Méthode permettant de récuperer le texte à afficher à un joueur quand il reçoit sa récompense
     * @return
     */
    public abstract String getRewardText();

    public Player getJoueur() {
        return joueur;
    }

    public void setJoueur(Player joueur) {
        this.joueur = joueur;
        this.mcPlayer = mineralcontest.plugin.getMCPlayer(joueur);
    }

    public MCPlayer getMcPlayer() {
        return mcPlayer;
    }

    /**
     * Méthode permettant de donner la récompense à un joueur
     */
    public void rewardPlayer() {
        // On informe le joueur qu'il a reçu une récompense
        getJoueur().sendMessage(mineralcontest.prefixPrive + getRewardText());
        giveToPlayer();
    }
}
