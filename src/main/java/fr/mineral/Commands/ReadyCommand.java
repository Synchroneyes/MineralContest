package fr.mineral.Commands;

import fr.mineral.Core.Game;
import fr.mineral.Core.Votemap;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            Game game = mineralcontest.plugin.getGame();
            Votemap votemap = game.votemap;
            Player joueur = (Player) commandSender;

            if(votemap.isVoteEnded()) {
                if(!game.isPlayerReady(joueur)) {
                    try {
                        game.setPlayerReady(joueur);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else game.removePlayerReady(joueur);
            } else {
                joueur.sendMessage("vote is not ended");
            }
        }

        return false;
    }
}
