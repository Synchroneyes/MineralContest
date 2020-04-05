package fr.mineral.Commands;

import fr.mineral.Core.Game.Game;
import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("leaveteam")) {
            World pluginWorld = mineralcontest.plugin.pluginWorld;
            Player player = (Player) commandSender;
            Game game = mineralcontest.plugin.getGame();
            if(pluginWorld.equals(player.getWorld())) {

                if(!game.isGameStarted() && !game.isPreGame() && !game.isGamePaused()) {
                    Equipe playerTeam = game.getPlayerTeam(player);
                    if(playerTeam != null) playerTeam.removePlayer(player);
                    return false;
                }

            }
        }
        return false;
    }
}
