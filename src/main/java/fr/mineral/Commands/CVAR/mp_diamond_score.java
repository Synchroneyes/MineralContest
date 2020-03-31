package fr.mineral.Commands.CVAR;

import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_diamond_score implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()){
                sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
                return true;
            }

            // début mp_diamond_score
            if(command.getName().equalsIgnoreCase("mp_diamond_score")) {
                if(args.length == 1) {
                    try {
                    /*mineralcontest.SCORE_DIAMOND = Integer.parseInt(args[0]);
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.cvar_diamond_score.toString() + Integer.parseInt(args[0]));
                    mineralcontest.plugin.setConfigValue("config.cvar.mp_diamond_score", mineralcontest.SCORE_DIAMOND);*/
                        GameSettingsCvar.SCORE_DIAMOND.setValue(args[0]);
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.cvar_diamond_score.toString() + Integer.parseInt(args[0]));

                        return false;
                    }catch (NumberFormatException nfe) {
                        sender.sendMessage("[mp_diamond_score] Incorrect value");
                        return true;
                    }
                } else if(args.length == 0) {
                    sender.sendMessage("[mp_diamond_score] Value: " + (int) GameSettingsCvar.SCORE_DIAMOND.getValue());
                    return true;
                }else {
                    sender.sendMessage("Usage: /mp_diamond_score <valeur | default: 150>");
                    return true;
                }
            }
            // FIN mp_diamond_score
            return false;
        }

        return false;

    }
}
