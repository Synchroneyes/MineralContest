package fr.mineral.Commands.Developper;

import fr.mineral.Core.House;
import fr.mineral.Utils.HouseSetup;
import fr.mineral.Utils.Save.FileToGame;
import fr.mineral.Utils.Setup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {

    /*
        dev command
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("setup")) {
            if(sender.isOp()) {

                if(args.length > 0 ) {
                    switch(args[0]) {
                        case "doors":
                            HouseSetup.addBlock = false;
                            HouseSetup.addDoors = true;
                            HouseSetup.addChest = false;
                            HouseSetup.addSpawn = false;
                            sender.sendMessage("Veuillez selectionner les portes");
                            break;

                        case "blocks":
                            HouseSetup.addBlock = true;
                            HouseSetup.addDoors = false;
                            HouseSetup.addChest = false;
                            HouseSetup.addSpawn = false;
                            sender.sendMessage("Veuillez selectionner les blocks");
                            break;

                        case "spawn":
                            HouseSetup.addBlock = false;
                            HouseSetup.addDoors = false;
                            HouseSetup.addChest = false;
                            HouseSetup.addSpawn = true;
                            sender.sendMessage("Veuillez selectionner le spawn");
                            break;

                        case "chest":
                            HouseSetup.addBlock = false;
                            HouseSetup.addDoors = false;
                            HouseSetup.addChest = true;
                            HouseSetup.addSpawn = false;
                            sender.sendMessage("Veuillez selectionner le coffre");
                            break;


                        default:
                            sender.sendMessage("options: blocks, doors, chest, spawn");
                            break;
                    }
                }
                /*if(Setup.instance == null) {
                    Setup s = new Setup();
                    Setup.displayInfos((Player) sender);
                }*/
            }
        }
        return false;
    }
}
