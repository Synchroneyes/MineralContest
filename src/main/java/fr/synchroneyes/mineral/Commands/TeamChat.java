package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Statistics.Class.TalkStat;
import fr.synchroneyes.mineral.Teams.Equipe;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Lang.error_command_can_only_be_used_in_game.toString());
            return false;
        }

        Player player = (Player) commandSender;
        if (mineralcontest.isInAMineralContestWorld(player) && !mineralcontest.plugin.pluginWorld.equals(player.getWorld())) {
            Game partie = mineralcontest.getPlayerGame(player);
            if (partie == null) {
                player.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            Equipe playerTeam = partie.getPlayerTeam(player);

            if ((command.getName().equalsIgnoreCase("t") || command.getName().equalsIgnoreCase("team")) && playerTeam != null) {
                StringBuilder message = new StringBuilder();
                for (String arg : strings)
                    message.append(arg + " ");

                playerTeam.sendMessage(message.toString(), player);

                partie.getStatsManager().register(TalkStat.class, player, null);

            }
        }


        return false;
    }
}
