package fr.mineral.Commands;

import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("join")) {
            if(mineralcontest.plugin.getGame().mp_randomize_team == 0) {
                if(args.length == 1) {
                    switch(args[0].toLowerCase()) {
                        case "j":
                        case "jaune":
                        case "y":
                        case "yellow":
                            try {
                                mineralcontest.plugin.getGame().getTeamJaune().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "b":
                        case "bleu":
                        case "bleue":
                        case "blue":
                            try {
                                mineralcontest.plugin.getGame().getTeamBleu().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "r":
                        case "rouge":
                        case "red":
                            try {
                                mineralcontest.plugin.getGame().getTeamRouge().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        default:
                            sender.sendMessage("Usage: /join <rouge | jaune | bleu>");
                            return true;
                    }
                }else{
                    sender.sendMessage("Usage: /join <rouge | jaune | bleu>");
                    return true;
                }
            } else {
                sender.sendMessage(mineralcontest.prefixErreur + "Les équipes seront attribué de manière aléatoire. Pour changer ça, l'admin doit entrer la commande: mp_randomize_team 0");
                return true;
            }
        }

        return false;
    }
}
