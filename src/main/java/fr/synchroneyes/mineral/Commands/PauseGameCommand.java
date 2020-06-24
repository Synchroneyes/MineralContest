package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PauseGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;


        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }
            if (command.getName().equalsIgnoreCase("pause")) {
                if (partie.isGameStarted()) {
                    if (partie.isGamePaused()) {
                        sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_already_paused.toString()));
                    } else {
                        partie.pauseGame();
                    }
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_not_started.toString()));
                }
            }
        }


        return false;
    }
}
