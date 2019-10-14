package fr.mineral.Commands;

import fr.mineral.Utils.GameToFile;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenDoor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("ouvrir")) {
            try {
                mineralcontest.plugin.getGame().getTeamBleu().addPlayerToTeam((Player)sender);

                GameToFile g = new GameToFile("world1");
                g.saveToFile();
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
