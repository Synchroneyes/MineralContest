package fr.mineral.Utils.Player;

import org.bukkit.entity.Player;

public class CouplePlayer {
    private int valeur;
    private Player joueur;

    public CouplePlayer(Player joueur, int valeur) {
        this.joueur = joueur;
        this.valeur = valeur;
    }

    public int getValeur() { return this.valeur; }
    public Player getJoueur() { return this.joueur; }

    public void setValeur(int v) { this.valeur = v;}

}
