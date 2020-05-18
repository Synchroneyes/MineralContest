package fr.groups.Utils;

public enum Etats {
    EN_ATTENTE("En attente"),
    VOTE_EN_COURS("Vote en cours"),
    VOTE_TERMINE("Vote terminé"),
    PREGAME("Démarrage de la partie"),
    GAME_EN_COURS("Partie en cours"),
    GAME_TERMINE("Partie terminée");

    private String nom;

    Etats(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
