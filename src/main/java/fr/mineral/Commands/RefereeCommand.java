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
        if(command.getName().equalsIgnoreCase("referee") || command.getName().equalsIgnoreCase("arbitre") ) {
            if(sender.isOp()) {
                Player player = (Player) sender;
                Game game = mineralcontest.plugin.getGame();
                if(!mineralcontest.plugin.getGame().isReferee(player)) {
                    player.sendMessage(mineralcontest.prefixPrive + Lang.now_referee.toString());
                    game.addReferee(player);
                    return false;
                } else {
                    player.sendMessage(mineralcontest.prefixPrive + Lang.no_longer_referee.toString());
                    game.removeReferee(player);
                    return false;
                }
            }
        }
        return false;
    }
}
