package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
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
                if(mineralcontest.plugin.getGame().isReferee((Player) sender)) {
                    sender.sendMessage("You cant join a team, you are a referee");
                    return false;
                }
                if(args.length == 1) {
                    switch(args[0].toLowerCase()) {
                        case "j":
                        case "jaune":
                        case "y":
                        case "yellow":
                            try {
                                mineralcontest.plugin.getGame().getYellowHouse().getTeam().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "b":
                        case "bleu":
                        case "bleue":
                        case "blue":
                            try {
                                mineralcontest.plugin.getGame().getBlueHouse().getTeam().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "r":
                        case "rouge":
                        case "red":
                            try {
                                mineralcontest.plugin.getGame().getRedHouse().getTeam().addPlayerToTeam((Player)sender);

                            }catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        default:
                            sender.sendMessage("Usage: /join <"+ Lang.red_team.toString() + " | "+ Lang.yellow_team.toString() + " | "+ Lang.blue_team.toString() + ">");
                            return true;
                    }
                }else{
                    sender.sendMessage("Usage: /join <"+ Lang.red_team.toString() + " | "+ Lang.yellow_team.toString() + " | "+ Lang.blue_team.toString() + ">");
                    return true;
                }
            } else {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.admin_team_will_be_randomized.toString() ));
                return true;
            }
        }

        return false;
    }
}
