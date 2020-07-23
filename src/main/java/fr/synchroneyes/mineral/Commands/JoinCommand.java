package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Core.House;
import fr.synchroneyes.mineral.Settings.GameSettings;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.Utils.ChatColorString;
import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.mineralcontest;
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
            if (partie == null || (partie.groupe != null && partie.groupe.getMonde() == null)) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            GameSettings settings = partie.groupe.getParametresPartie();

            if (command.getName().equalsIgnoreCase("join")) {
                try {
                    if (partie.isGameStarted() && !partie.isGamePaused() || partie.isPreGame()) return false;
                    if (settings.getCVAR("mp_randomize_team").getValeurNumerique() == 0) {


                        if (args.length == 0) {
                            partie.openTeamSelectionMenuToPlayer(player);
                            return false;
                        }

                        if (args.length == 1) {
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

                            Equipe defaultPlayerTeam = partie.getPlayerTeam(player);
                            if (defaultPlayerTeam != null) defaultPlayerTeam.removePlayer(player);

                            try {
                                if (partie.isReferee((Player) sender)) {
                                    partie.removeReferee((Player) sender, false);
                                }

                                selectedHouse.getTeam().addPlayerToTeam(player, false, !(partie.isGameStarted() || partie.isGamePaused()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Error.Report(e, partie);
                                return false;
                            }


                            return false;
                        }

                    } else {
                        sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.admin_team_will_be_randomized.toString()));
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Error.Report(e, partie);
                }
            }
        }


        return false;
    }


}
