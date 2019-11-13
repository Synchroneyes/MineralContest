package fr.mineral.Commands.CVAR;

import fr.mineral.Utils.Metric.SendInformation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_enable_metrics implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("mp_enable_metrics")) {
            if(args.length == 1) {
                int valeur = 1;
                try {
                    valeur = Integer.parseInt(args[0]);
                    switch(valeur) {
                        case 0:
                            SendInformation.disable();
                            break;
                        case 1:
                            SendInformation.enable();
                            break;

                        default:
                            sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
                            break;
                    }
                }catch (NumberFormatException nfe){
                    nfe.printStackTrace();
                    sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
                }
                
            } else {
                sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
            }
        }
        return false;
    }
}
