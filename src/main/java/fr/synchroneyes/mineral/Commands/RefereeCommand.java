package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RefereeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // todo
        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            if (command.getName().equalsIgnoreCase("referee") || command.getName().equalsIgnoreCase("arbitre")) {
                if (sender.isOp()) {
                    Game game = mineralcontest.getPlayerGame(player);
                    if (game == null) {
                        sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                        return false;
                    }
                    if (!game.isReferee(player)) {
                        game.addReferee(player);
                        return false;
                    } else {

                        try {
                            game.removeReferee(player);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }
}
