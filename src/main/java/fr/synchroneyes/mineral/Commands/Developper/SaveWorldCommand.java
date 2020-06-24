package fr.synchroneyes.mineral.Commands.Developper;

import fr.synchroneyes.mineral.Utils.Save.GameToFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveWorldCommand implements CommandExecutor {
    /*
        Dev command only
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("saveworld")) {
            if (args.length == 0) {
                try {
                    GameToFile g = new GameToFile("nouveau_biome");
                    g.saveToFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                sender.sendMessage("usage: /saveworld");
                return true;
            }
        }
        return false;
    }
}
