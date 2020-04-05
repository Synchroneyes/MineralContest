package fr.mineral.Events;

import fr.mineral.Core.Game.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        World worldEvent = event.getPlayer().getWorld();
        if(worldEvent.equals(mineralcontest.plugin.pluginWorld)) {
            Player sender = event.getPlayer();
            Game partie = mineralcontest.plugin.getGame();

            if(partie.votemap.isVoteEnded() &&
                    !partie.isGameStarted() &&
                    !partie.isGamePaused() &&
                    !partie.isGameEnded()) {

                if(partie.isPlayerReady(sender)) {
                    event.setFormat(ChatColor.GREEN + Lang.ready_tag.toString() + ChatColor.WHITE + "%s:" + ChatColor.RESET + " %s" );
                } else {
                    event.setFormat(ChatColor.RED + Lang.not_ready_tag.toString() + ChatColor.WHITE + "%s:" + ChatColor.RESET + " %s" );

                }
                return;
            }

            if(partie.isReferee(sender)) {
                event.setFormat(ChatColor.GOLD + "[☆] %s:" + ChatColor.RESET + " %s" );
                return;
            }

            Equipe team = mineralcontest.plugin.getGame().getPlayerTeam(sender);
            if(team != null) {
                event.setFormat(team.getCouleur() + "%s:" + ChatColor.RESET + " %s" );
            }

        }

    }
}
