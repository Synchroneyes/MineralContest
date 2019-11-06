package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_iron_score implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().isGameStarted()){
            sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
            return true;
        }

        // début mp_iron_score
        if(command.getName().equalsIgnoreCase("mp_iron_score")) {
            if(args.length == 1) {
                try {
                    Game.SCORE_IRON = Integer.parseInt(args[0]);
                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Le score pour le fer a été mis à " + Integer.parseInt(args[0]));
                    return false;
                }catch (NumberFormatException nfe) {
                    sender.sendMessage("[mp_iron_score] La valeur doit être un nombre");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_iron_score] Valeur actuelle: " + Game.SCORE_IRON);
                return true;
            }else {
                sender.sendMessage("Usage: /mp_iron_score <valeur | default: 10>");
                return true;
            }
        }
        // FIN mp_iron_score
        return false;
    }
}
