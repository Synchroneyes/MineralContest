package fr.mineral.Commands;

import fr.mineral.Core.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefereeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("referee") || command.getName().equalsIgnoreCase("arbitre") ) {
                if(sender.isOp()) {
                    Game game = mineralcontest.plugin.getGame();
                    if(!mineralcontest.plugin.getGame().isReferee(player)) {
                        game.addReferee(player);
                        return false;
                    } else {

                        if(game.isGameStarted() || game.isPreGame()) {
                            player.sendMessage(mineralcontest.prefixPrive + Lang.cant_remove_admin_game_in_progress.toString());
                            return false;
                        }
                        game.removeReferee(player);
                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }
}
