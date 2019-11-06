package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_randomize_team implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if(mineralcontest.plugin.getGame().isGameStarted()){
            sender.sendMessage(mineralcontest.prefixErreur + "La partie est déjà en cours, la modification de valeur n'est pas permis.");
            return true;
        }

        // début mp_randomizer_team
        if(command.getName().equalsIgnoreCase("mp_randomize_team")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("1")) {
                    mineralcontest.plugin.getGame().mp_randomize_team = Integer.parseInt(args[0]);
                    switch(Integer.parseInt(args[0])) {
                        case 1:
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Les équipes seront désormais aléatoire.");
                            break;

                        case 0:
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Les équipes seront désormais de manière manuelle.");
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + "Veuillez utiliser /join <rouge | bleu | jaune>");
                            break;
                    }

                    return false;
                }else{
                    sender.sendMessage("Usage: /mp_randomize_team [1 | 0]");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_randomize_team] Valeur actuelle: " + mineralcontest.plugin.getGame().mp_randomize_team);
                return true;
            }else {
                sender.sendMessage("Usage: /mp_randomize_team [1 | 0]");
                return true;
            }
        }
        // FIN mp_randomize_team

        return false;
    }
}
