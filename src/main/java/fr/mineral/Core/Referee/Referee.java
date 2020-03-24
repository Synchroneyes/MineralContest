package fr.mineral.Core.Referee;

import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Stack;

public class Referee {

    /*
    ITEMS:
        Liste des scores (avec ou sans points)
        Demarrer
        Pause
        Stop

     */

    public static Player refereeForcingVote;

    public static void forceVote(Player refereeForcingVote) {
        Referee.refereeForcingVote = refereeForcingVote;
    }

    public static void broadcastToReferees(String message) {
        for(Player referee: mineralcontest.plugin.getGame().getReferees())
            referee.sendMessage(message);
    }

    // Display leaderboard to all referees
    public static void displayLeaderboard() {
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<Equipe> equipes = new LinkedList<>();
        Stack<Equipe> leaderboard = new Stack<>();

        equipes.add(mineralcontest.plugin.getGame().getBlueHouse().getTeam());
        equipes.add(mineralcontest.plugin.getGame().getRedHouse().getTeam());
        equipes.add(mineralcontest.plugin.getGame().getYellowHouse().getTeam());

        int minScore = Integer.MIN_VALUE;
        Equipe highestTeam = null;
        while(equipes.size() > 0) {
            for(Equipe equipe : equipes) {
                if(equipe.getScore() >= minScore) {
                    minScore = equipe.getScore();
                    highestTeam = equipe;
                }
            }

            equipes.remove(highestTeam);
            minScore = Integer.MIN_VALUE;
            leaderboard.add(highestTeam);
        }


        stringBuilder.append("=== " + Lang.referee_item_leaderboard.toString() + " ===\n");
        int position = 3;
        while (!leaderboard.empty()) {
            highestTeam = leaderboard.pop();
            stringBuilder.append(position + " - " + Lang.translate(Lang.team_score.toString(), highestTeam) + "\n");
            position--;
        }
        stringBuilder.append(ChatColor.WHITE + "===========\n");


        broadcastToReferees(stringBuilder.toString());
        return;
    }

    public static void displayTeamScore() {
        LinkedList<Equipe> equipes = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        equipes.add(mineralcontest.plugin.getGame().getBlueHouse().getTeam());
        equipes.add(mineralcontest.plugin.getGame().getRedHouse().getTeam());
        equipes.add(mineralcontest.plugin.getGame().getYellowHouse().getTeam());

        for(Equipe equipe : equipes) {
            stringBuilder.append(equipe.getCouleur() + equipe.getNomEquipe() + ": " + equipe.getScore() + " points\n" + ChatColor.WHITE);
        }

        broadcastToReferees(stringBuilder.toString());

        return;
    }
}
