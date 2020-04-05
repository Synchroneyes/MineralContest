package fr.mineral.Commands.CVAR;

import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_team_max_players implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()){
                sender.sendMessage(mineralcontest.prefixErreur + Lang.kick_game_already_in_progress.toString());
                return true;
            }

            // début mp_team_max_players
            if(command.getName().equalsIgnoreCase("mp_team_max_players")) {
                if(args.length == 1) {
                    try {

                        if(Integer.parseInt(args[0]) > 5) {
                            sender.sendMessage("[mp_team_max_players] Must be less than 5");
                            return true;
                        }

                        if(Integer.parseInt(args[0]) < 1) {
                            sender.sendMessage("[mp_team_max_players] Must be higher than 1");
                            return true;
                        }

                    /*mineralcontest.mp_team_max_player = Integer.parseInt(args[0]);
                    mineralcontest.plugin.setConfigValue("config.cvar.mp_team_max_player", mineralcontest.mp_team_max_player);*/
                        int mp_team_max_players = Integer.parseInt(args[0]);
                        GameSettingsCvar.mp_team_max_player.setValue(mp_team_max_players);


                        mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.cvar_team_max_player.toString()));
                        mineralcontest.plugin.getGame().votemap.enableVote(false);
                        return false;
                    }catch (NumberFormatException nfe) {
                        sender.sendMessage("[mp_team_max_players] La valeur doit être un nombre inferieur à 5");
                        return true;
                    }
                } else if(args.length == 0) {
                    sender.sendMessage("[mp_team_max_players] Valeur actuelle: " + GameSettingsCvar.mp_team_max_player.getValue());
                    return true;
                }else {
                    sender.sendMessage("Usage: /mp_team_max_players <valeur | default: 2>");
                    return true;
                }
            }
        }

        // FIN mp_team_max_players
        return false;
    }
}
