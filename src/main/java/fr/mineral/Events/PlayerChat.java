package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ChatColorString;
import fr.mineral.Utils.Log.GameLogger;
import fr.mineral.Utils.Log.Log;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class PlayerChat implements Listener {


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if (mineralcontest.isAMineralContestWorld(worldEvent)) {
            Player sender = event.getPlayer();
            Game partie = mineralcontest.getWorldGame(worldEvent);


            Set<Player> receveurs = event.getRecipients();
            receveurs.clear();
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (mineralcontest.isInAMineralContestWorld(online)) {
                    Game playerGame = mineralcontest.getPlayerGame(online);
                    if (playerGame == null) receveurs.add(online);
                    if (playerGame != null && playerGame.isGameStarted()) continue;
                    receveurs.add(online);
                }
            }




            /*if(partie.votemap.isVoteEnded() &&
                    !partie.isGameStarted() &&
                    !partie.isGamePaused() &&
                    !partie.isGameEnded()) {

                if(partie.isPlayerReady(sender)) {
                    event.setFormat(ChatColor.GREEN + Lang.ready_tag.toString() + ChatColor.WHITE + "%s:" + ChatColor.RESET + " %s" );
                } else {
                    event.setFormat(ChatColor.RED + Lang.not_ready_tag.toString() + ChatColor.WHITE + "%s:" + ChatColor.RESET + " %s" );

                }
                return;
            }*/

            if (partie == null) return;


            if(partie.isReferee(sender)) {
                event.setFormat(ChatColor.GOLD + "[☆] %s:" + ChatColor.RESET + " %s" );
            } else {

                Equipe team = partie.getPlayerTeam(sender);
                if (team != null) {
                    event.setFormat(team.getCouleur() + "%s:" + ChatColor.RESET + " %s");
                }
            }


            // On fait en sorte à ce que seul les personnes de la partie recoivent un message
            if (partie.isGameStarted() && !partie.isGamePaused()) {
                receveurs.clear();
                receveurs.addAll(partie.groupe.getPlayers());
            }


            GameLogger.addLog(new Log("player_chat", event.getPlayer().getDisplayName() + ": " + event.getMessage(), "player_chat"));

        }

    }
}
