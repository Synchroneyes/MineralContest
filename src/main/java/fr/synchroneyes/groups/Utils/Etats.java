package fr.synchroneyes.groups.Utils;

public enum Etats {
    EN_ATTENTE("En attente"),
    VOTE_EN_COURS("Vote en cours"),
    VOTE_TERMINE("Vote terminé"),
    PREGAME("Démarrage de la partie"),
    GAME_EN_COURS("Partie en cours"),
    GAME_TERMINE("Partie terminée"),
    ATTENTE_DEBUT_PARTIE("En attente du démarrage");

    private String nom;

    Etats(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }


}
