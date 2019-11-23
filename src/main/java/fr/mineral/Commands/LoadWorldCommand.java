package fr.mineral.Commands;

import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class LoadWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("loadWorld")) {
            if(args.length != 1) {
                sender.sendMessage("Usage: /load <worldname>");
            } else {
                FileToGame f = new FileToGame();
                try {
                    f.readFile(args[0]);

                }catch (IOException e) {
                    sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());

                }catch(Exception e) {
                    sender.sendMessage(mineralcontest.prefixErreur + e.getMessage());

                }
            }
        }
        return false;
    }
}
