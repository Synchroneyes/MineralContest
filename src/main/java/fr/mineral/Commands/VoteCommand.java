package fr.mineral.Commands;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(mineralcontest.plugin.getGame().votemap.voteEnabled) {
            Player joueur = (Player) sender;
            if(args.length == 1) {
                try {
                    mineralcontest.plugin.getGame().votemap.addPlayerVote(joueur, Integer.parseInt(args[0]));
                }catch(NumberFormatException nfe) {
                    joueur.sendMessage("Usage: /vote <biome> | " + Lang.vote_explain.toString());
                }
            }else{
                joueur.sendMessage("Usage: /vote <biome> | " + Lang.vote_explain.toString());
            }
        }
        return false;
    }
}
