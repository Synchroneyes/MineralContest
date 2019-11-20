package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
            sender.sendMessage(mineralcontest.prefixErreur + Lang.translate((String) mineralcontest.LANG.get("game_already_started")));
        } else {

            try {
                mineralcontest.plugin.getGame().demarrerPartie();
            } catch (Exception e) {
                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
}
