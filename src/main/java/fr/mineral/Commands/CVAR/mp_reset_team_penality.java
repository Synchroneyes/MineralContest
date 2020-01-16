package fr.mineral.Commands.CVAR;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_reset_team_penality implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.isOp() && command.getName().equals("mp_reset_team_penality")) {
            if (args.length == 1) {
                String usage = "Usage: /mp_reset_team_penality <" + Lang.red_team.toString() + " | " + Lang.yellow_team.toString() + " | " + Lang.blue_team.toString() + ">";

                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "j":
                        case "jaune":
                        case "y":
                        case "yellow":
                            try {
                                mineralcontest.plugin.getGame().getYellowHouse().getTeam().resetPenalty();

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "b":
                        case "bleu":
                        case "bleue":
                        case "blue":
                            try {
                                mineralcontest.plugin.getGame().getBlueHouse().getTeam().resetPenalty();

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        case "r":
                        case "rouge":
                        case "red":
                            try {
                                mineralcontest.plugin.getGame().getRedHouse().getTeam().resetPenalty();

                            } catch (Exception e) {
                                sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                            }
                            break;

                        default:
                            sender.sendMessage(usage);
                            return true;
                    }
                } else {
                    sender.sendMessage(usage);
                }
            }
            return false;
        }
        return true;
    }
}
