package fr.mineral.Commands;

import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player) commandSender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            Equipe playerTeam = mineralcontest.plugin.getGame().getPlayerTeam(player);

            if((command.getName().equalsIgnoreCase("t") || command.getName().equalsIgnoreCase("team")) && playerTeam != null) {
                StringBuilder message = new StringBuilder();
                for(String arg : strings)
                    message.append(arg + " " );

                playerTeam.sendMessage(message.toString(), player);
            }
        }

        return false;
    }
}
