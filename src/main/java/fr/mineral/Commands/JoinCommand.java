package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Core.House;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Teams.Equipe;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ChatColorString;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;


        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if(command.getName().equalsIgnoreCase("join")) {
                if ((int) GameSettingsCvarOLD.mp_randomize_team.getValue() == 0) {
                    if (partie.isReferee((Player) sender)) {
                        sender.sendMessage("You cant join a team, you are a referee");
                        return false;
                    }

                    if(args.length == 1) {
                        House selectedHouse = partie.getHouseFromName(args[0]);
                        if (selectedHouse == null) {
                            StringBuilder availableTeam = new StringBuilder();

                            for (House maison : partie.getHouses()) {
                                availableTeam.append(ChatColorString.toString(maison.getTeam().getCouleur()) + ", ");
                            }

                            String chaineEquipes = availableTeam.toString();
                            chaineEquipes = chaineEquipes.substring(0, chaineEquipes.length() - 2);

                            player.sendMessage(mineralcontest.prefixErreur + Lang.cvar_error_invalid_team_name.toString());
                            player.sendMessage(Lang.team_available_list_text.toString());
                            player.sendMessage(chaineEquipes);

                            return false;
                        }

                        if (selectedHouse.getTeam().getJoueurs().size() >= (int) GameSettingsCvarOLD.mp_team_max_player.getValue()) {
                            player.sendMessage(mineralcontest.prefixErreur + Lang.team_is_full.toString());
                            return false;
                        }

                        Equipe defaultPlayerTeam = partie.getPlayerTeam(player);
                        if (defaultPlayerTeam != null) defaultPlayerTeam.removePlayer(player);

                        try {
                            selectedHouse.getTeam().addPlayerToTeam(player, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Error.Report(e, partie);
                            return false;
                        }


                        return false;
                    }
                } else {
                    sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.admin_team_will_be_randomized.toString() ));
                    return true;
                }
            }
        }



        return false;
    }
}
