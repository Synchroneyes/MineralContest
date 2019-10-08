package fr.mineral.Events;

import fr.mineral.Scoreboard.ScoreboardUtil;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;


public class PlayerJoin implements Listener {

    public void updateScoreboard() {




        /*if(mineralcontest.isGameStarted()) {
            for(Player online : Bukkit.getOnlinePlayers())
                ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", "Temps restant", mineralcontest.getTempsRestant(), "","Equipe " + mineralcontest.plugin.getPlayerTeam(online).getNomEquipe().toUpperCase());
        } else {
            for(Player online : Bukkit.getOnlinePlayers()) {
                Equipe equipe = mineralcontest.plugin.getPlayerTeam(online);
                // Si le joueur n'a pas d'équipe
                if(equipe == null) {
                    ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", mineralcontest.GAME_WAITING_START, "", "Pas dans une équipe");
                } else {
                    // Le joueur a une equipe
                    ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ", mineralcontest.GAME_WAITING_START, "",  "Equipe " + equipe.getNomEquipe().toUpperCase());
                }
                ScoreboardUtil.unrankedSidebarDisplay(online, "   MineralContest   ");
            }
        }*/
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.sendMessage("coucou");
        updateScoreboard();

    }

}
