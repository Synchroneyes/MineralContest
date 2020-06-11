package fr.mineral.Core.Referee;

import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.Stack;

public class Referee {


    public static Player refereeForcingVote;

    public static void forceVote(Player refereeForcingVote) {
        Referee.refereeForcingVote = refereeForcingVote;
    }



    // Display leaderboard to all referees
    public static void displayLeaderboard() {
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<Equipe> equipes = new LinkedList<>();
        Stack<Equipe> leaderboard = new Stack<>();


        // TODO

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


        //broadcastToReferees(stringBuilder.toString());
        return;
    }

    public static void displayTeamScore() {
        LinkedList<Equipe> equipes = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        /*equipes.add(mineralcontest.getPlayerGame(joueur).getBlueHouse().getTeam());
        equipes.add(mineralcontest.getPlayerGame(joueur).getRedHouse().getTeam());
        equipes.add(mineralcontest.getPlayerGame(joueur).getYellowHouse().getTeam());*/
        // TODO

        for(Equipe equipe : equipes) {
            stringBuilder.append(equipe.getCouleur() + equipe.getNomEquipe() + ": " + equipe.getScore() + " points\n" + ChatColor.WHITE);
        }

        //broadcastToReferees(stringBuilder.toString());

        return;
    }

    /**
     * Récupère le livre d'arbitrage
     */
    public static ItemStack getRefereeItem() {
        ItemStack item = new ItemStack(Material.BOOK, 1);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Lang.referee_item_name.toString());
        item.setItemMeta(itemMeta);
        return item;
    }
}
