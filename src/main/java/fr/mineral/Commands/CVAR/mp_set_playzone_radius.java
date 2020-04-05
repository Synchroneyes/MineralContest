package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game.Game;
import fr.mineral.Settings.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_set_playzone_radius implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // début mp_set_playzone_radius
        if(command.getName().equalsIgnoreCase("mp_set_playzone_radius")) {
            if(args.length == 1) {
                try {
                    Game game = mineralcontest.plugin.getGame();

                    if(Integer.parseInt(args[0]) < 1) {
                        sender.sendMessage("[mp_set_playzone_radius] Must be higher than 1");
                        return true;
                    }

                    int mp_set_playzone_radius = Integer.parseInt(args[0]);
                    GameSettingsCvar.mp_set_playzone_radius.setValue(mp_set_playzone_radius);

                    if(game.isGameStarted() || game.isPreGame() || game.isGamePaused()) {
                        try {
                            mineralcontest.plugin.setWorldBorder();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    mineralcontest.broadcastMessage(mineralcontest.prefixGlobal + Lang.translate(Lang.cvar_play_zone_radius.toString()));
                    mineralcontest.plugin.getGame().votemap.enableVote(false);
                    return false;
                }catch (NumberFormatException nfe) {
                    sender.sendMessage("[mp_set_playzone_radius] La valeur doit être un nombre superieur à 1");
                    return true;
                }
            } else if(args.length == 0) {
                sender.sendMessage("[mp_set_playzone_radius] Valeur actuelle: " + GameSettingsCvar.mp_set_playzone_radius.getValue());
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
