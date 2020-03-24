package fr.mineral.Commands.CVAR;

import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Utils.Metric.SendInformation;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class mp_enable_item_drop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("mp_enable_item_drop")) {
                if(sender.isOp()) {
                    if(args.length == 1) {
                        int valeur = 1;
                        try {
                            valeur = Integer.parseInt(args[0]);
                            if(valeur > 2 || valeur < 0) {
                                sender.sendMessage("Usage: /mp_enable_item_drop <0=NONE, 1=INGOTS, 2=ALL>");
                                return false;
                            }

                            GameSettingsCvar.mp_enable_item_drop.setValue(valeur);

                        /*mineralcontest.mp_enable_item_drop = valeur;
                        mineralcontest.plugin.setConfigValue("config.cvar.mp_enable_item_drop", mineralcontest.mp_enable_item_drop);*/

                            sender.sendMessage(mineralcontest.prefixPrive + "mp_enable_item_drop value is now " + valeur);
                            return false;
                        }catch (NumberFormatException nfe){
                            nfe.printStackTrace();
                            sender.sendMessage("Usage: /mp_enable_item_drop <0=NONE, 1=INGOTS, 2=ALL>");
                            return false;
                        }

                    } else {
                        sender.sendMessage("Usage: /mp_enable_item_drop <0=NONE, 1=INGOTS, 2=ALL>");
                        return false;
                    }

                }
            }

            return false;
        }

        return false;
    }
}
