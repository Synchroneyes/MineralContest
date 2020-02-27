package fr.mineral.Commands.CVAR;

import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_enable_item_drop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mp_enable_item_drop")) {
            if(sender.isOp()) {
                if(args.length == 1) {
                    int valeur = 1;
                    try {
                        valeur = Integer.parseInt(args[0]);
                        mineralcontest.plugin.getGame().mp_enable_item_drop = valeur;
                        return false;
                    }catch (NumberFormatException nfe){
                        nfe.printStackTrace();
                        sender.sendMessage("Usage: /mp_enable_item_drop <1 ou 0>");
                        return false;
                    }

                } else {
                    sender.sendMessage("Usage: /mp_enable_item_drop <1 ou 0>");
                    return false;
                }

            }
        }
        return false;
    }
}
