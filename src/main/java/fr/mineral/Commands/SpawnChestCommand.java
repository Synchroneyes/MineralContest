package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            if(command.getName().equalsIgnoreCase("spawnchest")) {
                Player p = (Player) commandSender;
                Game game = mineralcontest.plugin.getGame();
                if(p.isOp()) {
                    if(game.isGameStarted()) {
                        try {
                            game.getArene().getCoffre().spawn();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_only_when_game_is_started.toString());
                        return false;
                    }
                }

                return false;
            }
        } else {
            commandSender.sendMessage("This command can only be executed in game.");
            return false;
        }

        return false;
    }
}
