package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AllowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Game game = mineralcontest.plugin.getGame();


        if(command.getName().equalsIgnoreCase("allow")) {
            if(commandSender.isOp()) {
                if(args.length == 1 ){
                    String playerName = args[0];
                    if(!game.allowPlayerLogin(playerName)) {
                        commandSender.sendMessage(playerName + " did not tried to join the game");
                        return false;
                    }

                    commandSender.sendMessage(playerName + " is now allowed to join");
                    return false;
                } else {
                    commandSender.sendMessage("Usage: /allow <playername>");
                    return false;
                }
            }
        }


        return false;
    }
}
