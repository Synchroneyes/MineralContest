package fr.mineral.Commands;

import fr.mineral.Core.Referee.Referee;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /*Player player = (Player) sender;
        if(mineralcontest.isInAMineralContestWorld(player)) {
            if(mineralcontest.getPlayerGame(player).votemap.voteEnabled) {
                if(args.length == 1) {
                    try {

                        if(mineralcontest.getPlayerGame(player).isReferee(player) && Referee.refereeForcingVote != null && Referee.refereeForcingVote.equals(player)) {
                            mineralcontest.getPlayerGame(player).votemap.setSelectedBiome(Integer.parseInt(args[0]));
                            return false;
                        }

                        mineralcontest.getPlayerGame(player).votemap.addPlayerVote(player, Integer.parseInt(args[0]), false);
                    }catch(NumberFormatException nfe) {
                        player.sendMessage("Usage: /vote <biome> | " + Lang.vote_explain.toString());
                    }
                }else{
                    player.sendMessage("Usage: /vote <biome> | " + Lang.vote_explain.toString());
                }
            } else {
                player.sendMessage(mineralcontest.prefixErreur + Lang.vote_not_enabled.toString());
                if(player.isOp())
                    player.sendMessage(mineralcontest.prefixAdmin + Lang.admin_how_to_enable_vote.toString());
                return false;
            }
        }


        return false;
    }*/
        return false;
    }
}
