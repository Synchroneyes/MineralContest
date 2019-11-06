package fr.mineral.Commands;

import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("stopGame")) {
            if(sender.isOp()) {
                // On est jamais trop prudent ...
                if(mineralcontest.plugin.getGame().isGameStarted()) {
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + ChatColor.RED + "La partie a été arrêté par un ADMIN !");
                    mineralcontest.plugin.getGame().terminerPartie();
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + "La partie n'est pas encore démarrée.");
                }
            }
        }
        return false;
    }
}
