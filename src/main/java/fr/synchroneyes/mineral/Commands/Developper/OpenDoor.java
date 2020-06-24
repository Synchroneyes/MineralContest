package fr.synchroneyes.mineral.Commands.Developper;

import fr.synchroneyes.mineral.Utils.ErrorReporting.Error;
import fr.synchroneyes.mineral.Utils.Save.FileToGame;
import fr.synchroneyes.mineral.Utils.Save.GameToFile;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OpenDoor implements CommandExecutor {

    /*
        Disabled command, used in testing
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ouvrir")) {
            try {
                FileToGame g = new FileToGame();
                try {
                    GameToFile f = new GameToFile("world1");
                } catch (Exception e) {
                    mineralcontest.plugin.getServer().getLogger().info("ERREUR");
                    e.printStackTrace();
                    Error.Report(e, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //mineralcontest.getPlayerGame(joueur).getPortes().openDoor();
        }

        /*if(command.getName().equals("fermer")) {
            mineralcontest.getPlayerGame(joueur).getPortes().closeDoor();
        }*/
        return false;
    }
}
