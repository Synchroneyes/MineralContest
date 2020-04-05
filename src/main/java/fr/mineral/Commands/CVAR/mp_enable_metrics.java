package fr.mineral.Commands.CVAR;

import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_enable_metrics implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("mp_enable_metrics")) {
                if(args.length == 1) {
                    int valeur = 1;
                    try {
                        valeur = Integer.parseInt(args[0]);

                        if(valeur == 0 || valeur == 1) {
                        /*mineralcontest.mp_enable_metrics = valeur;
                        mineralcontest.plugin.setConfigValue("config.cvar.mp_enable_metrics", mineralcontest.mp_enable_metrics);*/
                            GameSettingsCvar.mp_enable_metrics.setValue(valeur);
                            if(valeur == 0) mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.metrics_are_now_disabled.toString());
                            else mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.metrics_are_now_enabled.toString());
                        }else {
                            sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
                        }

                        return false;



                    }catch (NumberFormatException nfe){
                        nfe.printStackTrace();
                        sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
                    }

                } else {
                    sender.sendMessage("Usage: /mp_enable_metrics <1 ou 0>");
                }
            }
        }

        return false;
    }
}
