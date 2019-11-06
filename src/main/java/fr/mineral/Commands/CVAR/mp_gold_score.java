package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_gold_score implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted()){
            sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
            return true;
        }

        // début mp_score_gold
        if(command.getName().equalsIgnoreCase("mp_gold_score")) {
            if(args.length == 1) {
                try {
                    Game.SCORE_GOLD = Integer.parseInt(args[0]);
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Le score pour l'or a été mis à " + Integer.parseInt(args[0]));
                    return false;
                }catch (NumberFormatException nfe) {
                    sender.sendMessage("[mp_gold_score] La valeur doit être un nombre");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_gold_score] Valeur actuelle: " + Game.SCORE_GOLD);
                return true;
            }else {
                sender.sendMessage("Usage: /mp_gold_score <valeur | default: 50>");
                return true;
            }
        }
        // FIN mp_score_gold
        return false;
    }
}
