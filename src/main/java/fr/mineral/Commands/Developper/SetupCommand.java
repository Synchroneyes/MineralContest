package fr.mineral.Commands.Developper;

import fr.mineral.Utils.Setup;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    /*
        dev command
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if (mineralcontest.plugin.getGame().isGameStarted() || mineralcontest.plugin.getGame().isGameInitialized)
                return false;
            if (command.getName().equalsIgnoreCase("setup")) {
                if (sender.isOp()) {
                    if (Setup.instance == null) {
                        Setup s = new Setup();
                        Setup.displayInfos((Player) sender);
                    }
                }
            }
        }
        return false;
    }
}
