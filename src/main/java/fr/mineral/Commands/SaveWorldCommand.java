package fr.mineral.Commands;

import fr.mineral.Utils.Save.GameToFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveWorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("saveWorld")) {
            if(args.length == 1) {
                try {
                    GameToFile g = new GameToFile(args[0]);
                    g.saveToFile();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                sender.sendMessage("usage: /saveworld <worldname>");
                return true;
            }
        }
        return false;
    }
}
