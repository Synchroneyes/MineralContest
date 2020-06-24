package fr.synchroneyes.mineral.Utils.Player;

import fr.synchroneyes.mineral.Teams.Equipe;

public class CouplePlayerTeam {
    private Equipe team;
    private String joueur;

    public CouplePlayerTeam(String j, Equipe e) {
        this.joueur = j;
        this.team = e;
    }

    public String getJoueur() {
        return this.joueur;
    }

    public Equipe getTeam() {
        return this.team;
    }


}
