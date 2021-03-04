package fr.synchroneyes.achievements.Rewards;


import fr.synchroneyes.mineral.Teams.Equipe;

/**
 * Classe permettant de donner des points à un joueur
 */
public class PointsReward extends AbstractReward{

    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void giveToPlayer() {
        // On récupère l'équipe du joueur
        Equipe equipe = getMcPlayer().getEquipe();
        if(equipe == null) return;


        // ON récupère le score de l'équipe
        int old_score = equipe.getScore();
        old_score += getPoints();

        equipe.setScore(old_score);
    }

    @Override
    public String getRewardText() {
        return "Vous avez reçu " + points + " points de récompense.";
    }
}
