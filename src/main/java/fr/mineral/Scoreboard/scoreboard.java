package fr.mineral.scoreboard;

import fr.mineral.Core.Equipe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.LinkedList;

public class scoreboard{
    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board = manager.getNewScoreboard();
    private LinkedList<Team> teams;
    private Objective healthObjective = board.registerNewObjective("showHealth", "health");
    private Objective scoreCount = board.registerNewObjective("score", "trigger");

    public  scoreboard(){
        // kill score
        scoreCount.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreCount.setDisplayName(" points");

        for (Player p : Bukkit.getOnlinePlayers()){
            p.setScoreboard(board);
            p.setHealth(p.getHealth());
        }

        // health on top of player head
        healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthObjective.setDisplayName(" / 20");
    }

    public void setTeamScore(LinkedList<Equipe> equipes){
        // pour chaque equipes crée avec la commande randomizeTeam
        // on crée une team et on ajoute cls joeurs dans la team

        for (Equipe e: equipes) {
            Team t = board.registerNewTeam(e.getNomEquipe());

            for(Player p : e.getJoueurs()){
                t.addEntry(p.getName());
            }

            t.setDisplayName(e.getNomEquipe());

            this.teams.add(t);
        }
    }


}