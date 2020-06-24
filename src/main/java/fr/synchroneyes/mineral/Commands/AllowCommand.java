package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            Player joueur = (Player) commandSender;
            Game game = mineralcontest.getPlayerGame(joueur);
            if (game == null) {
                commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }


            if (command.getName().equalsIgnoreCase("allow")) {
                if (commandSender.isOp()) {
                    if (args.length == 1) {
                        String playerName = args[0];
                        if (!game.allowPlayerLogin(playerName)) {
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
        }


        return false;
    }
}
