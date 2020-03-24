package fr.mineral.Commands.CVAR;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_add_team_penality implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if (sender.isOp() && command.getName().equals("mp_add_team_penality")) {
                String usage = "Usage: /mp_add_team_penality <" + Lang.red_team.toString() + " | " + Lang.yellow_team.toString() + " | " + Lang.blue_team.toString() + ">  <amount> ";
                if (args.length == 2) {

                    int valeur = 0;
                    try {
                        valeur = Integer.parseInt(args[1]);
                    }catch(Exception e) {
                        sender.sendMessage(usage);
                        return true;
                    }

                    switch (args[0].toLowerCase()) {
                        case "j":
                        case "jaune":
                        case "y":
                        case "yellow":
                            try {
                                mineralcontest.plugin.getGame().getYellowHouse().getTeam().addPenalty(valeur);

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "b":
                        case "bleu":
                        case "bleue":
                        case "blue":
                            try {
                                mineralcontest.plugin.getGame().getBlueHouse().getTeam().addPenalty(valeur);

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "r":
                        case "rouge":
                        case "red":
                            try {
                                mineralcontest.plugin.getGame().getRedHouse().getTeam().addPenalty(valeur);

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        default:
                            sender.sendMessage(usage);
                            return true;
                    }
                }
                return false;
            }
        }

        return true;
    }
}
