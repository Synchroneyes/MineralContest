package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) sender;
        if (mineralcontest.isInAMineralContestWorld(player) && !mineralcontest.plugin.pluginWorld.equals(player.getWorld())) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            if (partie.isGameStarted()) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_already_started.toString()));
                partie.getArene().chickenWaves.apparitionPoulets();
            } else {

                try {
                    Bukkit.getLogger().info("STARTING");
                    if(args.length == 1 && args[0].equals("force"))
                        partie.demarrerPartie(true);
                    else partie.demarrerPartie(false);

                } catch (Exception e) {
                    sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                    e.printStackTrace();
                    Error.Report(e, partie);
                }
            }
            return false;
        }else{
            sender.sendMessage("Only available on mineral contest map");
            return false;
        }

    }
}
