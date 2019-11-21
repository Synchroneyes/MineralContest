package fr.mineral.Core;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.CouplePlayer;
import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Votemap {

    private LinkedList<CouplePlayer> votant;
    public String[] biomes;

    public int voteNeige = 0;
    public int voteDesert = 0;
    public int voteForet = 0;
    public int votePlaine = 0;
    public int voteMontagne = 0;
    public int voteMarecage = 0;

    public boolean voteEnabled = false;

    public void enableVote() {
        this.voteEnabled = true;

        for(Player online : Bukkit.getOnlinePlayers())
            online.sendMessage(mineralcontest.prefixGlobal + Lang.get("vote_started"));
    }

    public void disableVote() {
        this.voteEnabled = false;
        voteNeige = 0;
        voteDesert = 0;
        voteForet = 0;
        votePlaine = 0;
        voteMontagne = 0;
        voteMarecage = 0;
        for(Player online : Bukkit.getOnlinePlayers())
            online.sendMessage(mineralcontest.prefixGlobal + Lang.get("vote_ended"));
    }


    public Votemap() {
        biomes = new String[6];
        biomes[0] = (String) Lang.vote_snow.toString();
        biomes[1] = (String) Lang.vote_desert.toString();
        biomes[2] = (String) Lang.vote_forest.toString();
        biomes[3] = (String) Lang.vote_plain.toString();
        biomes[4] = (String) Lang.vote_mountain.toString();
        biomes[5] = (String) Lang.vote_swamp.toString();
        this.votant = new LinkedList<CouplePlayer>();
    }


    public boolean havePlayerVoted(Player joueur) {
        for(CouplePlayer player : votant)
            if(player.getJoueur().equals(joueur))
                return true;

        return false;
    }

    public String getPlayerVote(Player joueur) {
        if(havePlayerVoted(joueur))
            for(CouplePlayer player : votant)
                if(player.getJoueur().equals(joueur))
                    return biomes[player.getValeur()];

        return "no player vote";
    }

    public boolean addPlayerVote(Player joueur, int numero_biome) {

        if(!voteEnabled) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.get("vote_not_enabled"));
            return false;
        }

        if(havePlayerVoted(joueur)) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.get("vote_already_voted"));
            return false;
        }

        if(numero_biome < 0 || numero_biome > biomes.length) {
            joueur.sendMessage(mineralcontest.prefixErreur + Lang.get(""));
            return false;
        }

        this.votant.add(new CouplePlayer(joueur, numero_biome));
        joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.get("vote_you_have_voted_for"), joueur));

        switch(numero_biome) {
            case 0: voteNeige++;  break;
            case 1: voteDesert++; break;
            case 2: voteForet++; break;
            case 3: votePlaine++; break;
            case 4: voteMontagne++; break;
            case 5: voteMarecage++; break;
        }

        if(allPlayerHaveVoted()){
            try {
                FileToGame fg = new FileToGame();
                fg.readFile(getWinnerBiome(false));
                disableVote();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }

    public String getWinnerBiome(boolean display) {
        int[] valeurs = new int[6];
        valeurs[0] = voteNeige;
        valeurs[1] = voteDesert;
        valeurs[2] = voteForet;
        valeurs[3] = votePlaine;
        valeurs[4] = voteMontagne;
        valeurs[5] = voteMarecage;

        int max = -1;
        int index = -1;
        for(int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] >= max) {
                max = valeurs[i];
                index = i;
            }
        }
        if(display) mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.get("vote_winning_biome")));
        return new String(biomes[index].toLowerCase());
    }

    private boolean allPlayerHaveVoted() {
        if(voteNeige + voteDesert + voteForet + votePlaine + voteMarecage + voteMontagne >= Bukkit.getOnlinePlayers().size())
            return true;
        return false;
    }

}
