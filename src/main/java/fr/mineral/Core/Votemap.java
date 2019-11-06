package fr.mineral.Core;

import fr.mineral.Utils.CouplePlayer;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Votemap {

    private LinkedList<CouplePlayer> votant;
    public String[] biomes = {"NEIGE", "DESERT", "FORET", "PLAINE", "MONTAGNE", "MARECAGE"};

    public int voteNeige = 0;
    public int voteDesert = 0;
    public int voteForet = 0;
    public int votePlaine = 0;
    public int voteMontagne = 0;
    public int voteMarecage = 0;

    public boolean voteEnabled = false;


    public void enableVote() {
        this.voteEnabled = true;
    }


    public Votemap() {
        this.votant = new LinkedList<CouplePlayer>();
    }


    public boolean havePlayerVoted(Player joueur) {
        for(CouplePlayer player : votant)
            if(player.getJoueur().equals(joueur))
                return true;

        return false;
    }

    public boolean addPlayerVote(Player joueur, int numero_biome) {

        if(!voteEnabled) {
            joueur.sendMessage(mineralcontest.prefixErreur + "Les votes ne sont pas actif");
            return false;
        }

        if(havePlayerVoted(joueur)) {
            joueur.sendMessage(mineralcontest.prefixErreur + "Vous avez déjà voté");
            return false;
        }

        if(numero_biome < 0 || numero_biome > biomes.length) {
            joueur.sendMessage(mineralcontest.prefixErreur + "Ce biome n'existe pas");
            return false;
        }

        this.votant.add(new CouplePlayer(joueur, numero_biome));
        joueur.sendMessage(mineralcontest.prefixPrive + "Vous avez voté pour le biome " + biomes[numero_biome]);

        switch(numero_biome) {
            case 0: voteNeige++;  break;
            case 1: voteDesert++; break;
            case 2: voteForet++; break;
            case 3: votePlaine++; break;
            case 4: voteMontagne++; break;
            case 5: voteMarecage++; break;
        }

        return true;
    }
}
