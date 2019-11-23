package fr.mineral.Commands;

import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SwitchCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("switch")) {
            if(sender.isOp()) {
                if(args.length == 2) {
                    try {
                        mineralcontest.plugin.getGame().switchPlayer(Bukkit.getPlayer(args[0]), args[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage("Usage: /switch <player> <team>");
                    return true;
                }
            } else {
                sender.sendMessage(mineralcontest.prefixErreur + "Vous n'avez pas accès à cette commande");
            }
        }

        return false;
    }
}
