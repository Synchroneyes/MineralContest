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


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if(mineralcontest.plugin.getGame().isGameStarted() && mineralcontest.plugin.getGame().isGamePaused() == false) {
            p.kickPlayer("Une partie est déjà en cours");
        }

    }

}
