package fr.mineral.Commands.CVAR;

import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_gold_score implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()){
                sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
                return true;
            }

            // début mp_score_gold
            if(command.getName().equalsIgnoreCase("mp_gold_score")) {
                if(args.length == 1) {
                    try {
                    /*mineralcontest.SCORE_GOLD = Integer.parseInt(args[0]);
                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.cvar_gold_score.toString() + Integer.parseInt(args[0]));
                    mineralcontest.plugin.setConfigValue("config.cvar.mp_gold_score", mineralcontest.SCORE_GOLD);*/

                        GameSettingsCvar.SCORE_GOLD.setValue(args[0]);
                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.cvar_gold_score.toString() + Integer.parseInt(args[0]));


                        return false;
                    }catch (NumberFormatException nfe) {
                        sender.sendMessage("[mp_gold_score] Incorrect value");
                        return true;
                    }
                } else if(args.length == 0) {
                    sender.sendMessage("[mp_gold_score] Value: " + (int) GameSettingsCvar.SCORE_GOLD.getValue());
                    return true;
                }else {
                    sender.sendMessage("Usage: /mp_gold_score <valeur | default: 50>");
                    return true;
                }
            }
        }

        // FIN mp_score_gold
        return false;
    }
}
