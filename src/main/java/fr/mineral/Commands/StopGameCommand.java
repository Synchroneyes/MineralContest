package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("stopGame")) {
                if(sender.isOp()) {
                    // On est jamais trop prudent ...
                    if(mineralcontest.plugin.getGame().isGameStarted()) {
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + ChatColor.RED + Lang.translate(Lang.game_over.toString()));
                        try {
                            mineralcontest.plugin.getGame().terminerPartie();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_not_started.toString()));
                    }
                }
            }
        }

        return false;
    }
}
