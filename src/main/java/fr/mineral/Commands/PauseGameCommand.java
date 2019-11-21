package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PauseGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("pause")) {
            if(mineralcontest.plugin.getGame().isGameStarted()) {
                if(mineralcontest.plugin.getGame().isGamePaused()) {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.get("game_already_paused")));
                } else {
                    mineralcontest.plugin.getGame().pauseGame();
                }
            } else {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.get("game_not_started")));
            }
        }

        return false;
    }
}
