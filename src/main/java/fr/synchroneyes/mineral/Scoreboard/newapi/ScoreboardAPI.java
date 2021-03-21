package fr.synchroneyes.mineral.Scoreboard.newapi;

import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ScoreboardAPI {

    private static ArrayList<Player> playersWithScoreboard;


    public static void createScoreboard(Player player, HashMap<ScoreboardFields, String> champs){

        // Création d'une liste de joueurs ayant un scoreboard
        if(playersWithScoreboard == null) playersWithScoreboard = new ArrayList<>();

        if(playersWithScoreboard.contains(player)) return;


        // Création d'un nouveau scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();






        // On traite les paramètres passé
        // Premier element, le titre
        String scoreboardTitle = "Mineral Contest";
        if(champs.containsKey(ScoreboardFields.SCOREBOARD_TITLE)) {
            scoreboardTitle = champs.get(ScoreboardFields.SCOREBOARD_TITLE);
        }


        Objective objective = scoreboard.registerNewObjective(ScoreboardFields.SCOREBOARD_TITLE.toString(), "dummy", scoreboardTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);

        addScoreboardText(player, "Mon text", 16);
        registerNewObjective(player, ScoreboardFields.SCOREBOARD_GROUP_STATE, "Ouvert", 15);
        addEmptyLine(player, 14);
        addScoreboardText(player, "Temps restant", 14);
        registerNewObjective(player, ScoreboardFields.SCOREBOARD_TIMELEFT, "Temps restant:", 13);



        player.setScoreboard(scoreboard);
        playersWithScoreboard.add(player);
    }

    public static void updateField(Player player, ScoreboardFields cle, String valeur){
        if(playersWithScoreboard == null) playersWithScoreboard = new ArrayList<>();
        if(!playersWithScoreboard.contains(player)) return;

        // On mets à jour le scoreboard du joueur
        Scoreboard scoreboard = player.getScoreboard();
        scoreboard.getObjective(ScoreboardFields.SCOREBOARD_TITLE.toString());

        // On mets le nombre de joueur en ligne
        Team team = scoreboard.getTeam(cle.toString());
        team.setPrefix(valeur);


    }

    public static void removeField(Player joueur, ScoreboardFields cle){

    }

    /**
     * Permet d'enregistrer un element dans le scoreboard d'un joueur
     * @param player
     * @param fields
     * @param value
     * @param position
     */
    private static void registerNewObjective(Player player, ScoreboardFields fields, String value, int position) {
        Scoreboard scoreboard = player.getScoreboard();

        Objective objective = scoreboard.getObjective(ScoreboardFields.SCOREBOARD_TITLE.toString());

        Team equipe = objective.getScoreboard().registerNewTeam(fields.toString());
        equipe.addEntry(fields.getUniqueColor());
        equipe.setPrefix(value);

        objective.getScore(fields.getUniqueColor()).setScore(position);

        player.setScoreboard(scoreboard);
    }

    private static void addScoreboardText(Player player, String text, int position) {
        Scoreboard scoreboard = player.getScoreboard();

        Objective objective = scoreboard.getObjective(ScoreboardFields.SCOREBOARD_TITLE.toString());


        Score score = objective.getScore(text);

        score.setScore(position);

        player.setScoreboard(scoreboard);
    }

    private static void addEmptyLine(Player player, int position) {
        Scoreboard scoreboard = player.getScoreboard();

        Objective objective = scoreboard.getObjective(ScoreboardFields.SCOREBOARD_TITLE.toString());


        String space = "";
        for(int i = 0; i < position; ++i)
            space += " ";

        Score score = objective.getScore(space);

        score.setScore(position);

        player.setScoreboard(scoreboard);
    }



}
