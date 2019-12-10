package fr.mineral.Commands.CVAR;

import fr.mineral.Core.Game;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_start_vote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("mp_start_vote")) {
            if(sender.isOp()) {
                Game game = mineralcontest.plugin.getGame();
                if(!game.isGameStarted() && !game.votemap.voteEnabled)
                    game.votemap.enableVote(true);
                    game.votemap.resetVotes();
            }
        }
        return false;
    }
}
