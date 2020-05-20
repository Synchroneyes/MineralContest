package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
import fr.mineral.Utils.ErrorReporting.Error;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(mineralcontest.plugin.getGame().isGameStarted()) {
                sender.sendMessage(mineralcontest.prefixErreur + Lang.translate(Lang.game_already_started.toString()));
            } else {

                try {
                    if(args.length == 1 && args[0].equals("force"))
                        mineralcontest.plugin.getGame().demarrerPartie(true);
                    else mineralcontest.plugin.getGame().demarrerPartie(false);

                } catch (Exception e) {
                    sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
                    e.printStackTrace();
                    Error.Report(e);
                }
            }
            return false;
        }else{
            sender.sendMessage("Only available on mineral contest map");
            return false;
        }

    }
}
