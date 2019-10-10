package fr.mineral.Commands;

import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted()) {
            sender.sendMessage(mineralcontest.prefixErreur + "Une partie est déjà en cours.");
        } else {
            try {
                mineralcontest.plugin.getGame().demarrerPartie();
            } catch (Exception e) {
                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
            }
        }
        return false;
    }
}
