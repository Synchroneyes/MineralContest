package fr.mineral.Commands;

import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.Utils.Save.GameToFile;
import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenDoor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("ouvrir")) {
            try {
                FileToGame g = new FileToGame();
                try {
                    GameToFile f = new GameToFile("world1");
                    //f.saveToFile();
                    //g.readFile("world1");
                }catch(Exception e) {
                    mineralcontest.plugin.getServer().getLogger().info("ERREUR");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //mineralcontest.plugin.getGame().getPortes().openDoor();
        }

        if(command.getName().equals("fermer")) {
            mineralcontest.plugin.getGame().getPortes().closeDoor();
        }
        return false;
    }
}
