package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvarOLD;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mp_set_playzone_radius implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // début mp_set_playzone_radius
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player)) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }


            Player joueur = (Player) sender;
            if(args.length == 1) {
                try {
                    Game game = mineralcontest.getPlayerGame(joueur);

                    if(Integer.parseInt(args[0]) < 1) {
                        sender.sendMessage("[mp_set_playzone_radius] Must be higher than 1");
                        return true;
                    }

                    int mp_set_playzone_radius = Integer.parseInt(args[0]);
                    GameSettingsCvarOLD.mp_set_playzone_radius.setValue(mp_set_playzone_radius);

                    if(game.isGameStarted() || game.isPreGame() || game.isGamePaused()) {
                        try {
                            mineralcontest.plugin.setWorldBorder();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.cvar_play_zone_radius.toString()), partie.groupe);
                    //mineralcontest.getPlayerGame(joueur).votemap.enableVote(false);
                    return false;
                }catch (NumberFormatException nfe) {
                    sender.sendMessage("[mp_set_playzone_radius] La valeur doit être un nombre superieur à 1");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_set_playzone_radius] Valeur actuelle: " + GameSettingsCvarOLD.mp_set_playzone_radius.getValue());
                return true;
            }else {
                sender.sendMessage("Usage: /mp_set_playzone_radius <valeur | default: 1000>");
                return true;
            }

        }

        // FIN mp_team_max_players
        return false;
    }
}
