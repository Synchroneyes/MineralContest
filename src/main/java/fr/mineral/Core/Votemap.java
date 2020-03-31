package fr.mineral.Core;

import fr.mineral.Core.Referee.Referee;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.Player.CouplePlayer;
import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.mineralcontest;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class Votemap {

    private LinkedList<CouplePlayer> votant;
    public String[] biomes;

    private boolean voteHasBeenEnabled = false;

    public int selectedBiome = -1;

    public int voteNeige = 0;
    public int voteDesert = 0;
    public int voteForet = 0;
    public int votePlaine = 0;
    public int voteMontagne = 0;
    public int voteMarecage = 0;



    public boolean voteEnabled = false;

    public void resetVotes() {
        this.votant.clear();
        voteNeige = 0;
        voteDesert = 0;
        voteForet = 0;
        votePlaine = 0;
        voteMontagne = 0;
        voteMarecage = 0;
        voteHasBeenEnabled = false;
    }

    public void removePlayerVote(Player p) {
        for(CouplePlayer player : votant)
            if(player.getJoueur().equals(p)) {
                switch (player.getValeur()) {
                    case 0:
                        voteNeige--;
                        break;
                    case 1:
                        voteDesert--;
                        break;
                    case 2:
                        voteForet--;
                        break;
                    case 3:
                        votePlaine--;
                        break;
                    case 4:
                        voteMontagne--;
                        break;
                    case 5:
                        voteMarecage--;
                        break;
                }
                votant.remove(player);
                return;
            }
    }

    public void enableVote(boolean force) {
        if((!mineralcontest.plugin.getGame().isGameStarted() &&
                (mineralcontest.plugin.pluginWorld.getPlayers().size() - mineralcontest.plugin.getGame().getRefereeCount()) == (int)GameSettingsCvar.mp_team_max_player.getValue() * 3 &&
                !voteHasBeenEnabled) || force){
            this.voteEnabled = true;

            biomes[0] = Lang.vote_snow.toString();
            biomes[1] = Lang.vote_desert.toString();
            biomes[2] = Lang.vote_forest.toString();
            biomes[3] = Lang.vote_plain.toString();
            biomes[4] = Lang.vote_mountain.toString();
            biomes[5] = Lang.vote_swamp.toString();

            for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
                online.sendMessage(mineralcontest.prefixGlobal + Lang.vote_started.toString());
        }
    }

    public void disableVote() {
        this.voteEnabled = false;
        voteNeige = 0;
        voteDesert = 0;
        voteForet = 0;
        votePlaine = 0;
        voteMontagne = 0;
        voteMarecage = 0;
        for(Player online : mineralcontest.plugin.pluginWorld.getPlayers())
            online.sendMessage(mineralcontest.prefixGlobal + Lang.vote_ended.toString());
    }


    public Votemap() {
        biomes = new String[6];
        biomes[0] = Lang.vote_snow.toString();
        biomes[1] = Lang.vote_desert.toString();
        biomes[2] = Lang.vote_forest.toString();
        biomes[3] = Lang.vote_plain.toString();
        biomes[4] = Lang.vote_mountain.toString();
        biomes[5] = Lang.vote_swamp.toString();
        this.votant = new LinkedList<>();
    }

    public void setSelectedBiome(int biome) {

        if(biome < 0 || biome > biomes.length) {
            Referee.broadcastToReferees(mineralcontest.prefixErreur + Lang.vote_selected_biome_doesnt_exist.toString());
            return;
        }

        resetVotes();
        for(Player joueur : mineralcontest.plugin.pluginWorld.getPlayers()) {
            addPlayerVote(joueur, biome, true);
        }
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

    public void addPlayerVote(Player joueur, int numero_biome, boolean forced) {
        if(!voteEnabled) {
            if(!forced) joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_not_enabled.toString());
            return;
        }

        if(havePlayerVoted(joueur)) {
            if(!forced) joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_already_voted.toString());
            return;
        }

        if(numero_biome < 0 || numero_biome > biomes.length) {
            if(!forced) joueur.sendMessage(mineralcontest.prefixErreur + Lang.vote_selected_biome_doesnt_exist.toString());
            return;
        }

        this.votant.add(new CouplePlayer(joueur, numero_biome));
        if(!forced) joueur.sendMessage(mineralcontest.prefixPrive + Lang.translate(Lang.vote_you_voted_for.toString(), joueur));

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
                voteHasBeenEnabled = true;
                FileToGame fg = new FileToGame();
                fg.readFile(getWinnerBiome());
                getWinnerBiome(true);
                disableVote();
                mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.set_yourself_as_ready_to_start_game.toString());

                mineralcontest.plugin.setWorldBorder();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isVoteEnded() {
        return voteHasBeenEnabled;
    }

    public String getWinnerBiome() {
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
        return "" + index;
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
        if(display) mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.vote_winning_biome.toString()));
        return new String(biomes[index].toLowerCase());
    }

    private int getVoteNumber() {
        return votant.size();
    }

    private boolean allPlayerHaveVoted() {
        if(getVoteNumber() >= mineralcontest.plugin.pluginWorld.getPlayers().size())
            return true;
        return false;
    }

}
