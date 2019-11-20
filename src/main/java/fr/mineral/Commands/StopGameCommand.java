package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
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
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + ChatColor.RED + Lang.translate((String) mineralcontest.LANG.get("game_over")));
                    try {
                        mineralcontest.plugin.getGame().terminerPartie();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate((String) mineralcontest.LANG.get("game_not_started")));
                }
            }
        }
        return false;
    }
}
