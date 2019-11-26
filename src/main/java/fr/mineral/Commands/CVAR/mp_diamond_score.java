package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_diamond_score implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted()){
            sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
            return true;
        }

        // début mp_diamond_score
        if(command.getName().equalsIgnoreCase("mp_diamond_score")) {
            if(args.length == 1) {
                try {
                    Game.SCORE_DIAMOND = Integer.parseInt(args[0]);
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + Lang.cvar_diamond_score.toString() + Integer.parseInt(args[0]));
                    return false;
                }catch (NumberFormatException nfe) {
                    sender.sendMessage("[mp_diamond_score] Incorrect value");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_diamond_score] Value: " + Game.SCORE_DIAMOND);
                return true;
            }else {
                sender.sendMessage("Usage: /mp_diamond_score <valeur | default: 150>");
                return true;
            }
        }
        // FIN mp_diamond_score
        return false;
    }
}
