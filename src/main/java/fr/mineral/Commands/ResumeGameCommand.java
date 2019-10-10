package fr.mineral.Commands;

import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResumeGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("resume")) {
            if(mineralcontest.plugin.getGame().isGamePaused()) {
                mineralcontest.plugin.getGame().resumeGame();
            }
        }
        return false;
    }
}
