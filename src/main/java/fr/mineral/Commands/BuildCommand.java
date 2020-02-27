package fr.mineral.Commands;

import fr.mineral.Core.MapBuilder.MapBuilderInventory;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("build")) {
            if(sender.isOp()) {
                if(mineralcontest.plugin.getGame().isGameStarted() || mineralcontest.plugin.getGame().isGamePaused()) {
                    sender.sendMessage("Game cant be started to build a map");
                    return false;
                }

                try {
                    MapBuilderInventory.initializeMapBuilderInventory();
                    MapBuilderInventory.getInstance().openInventory((Player) sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                sender.sendMessage("Admin Only !");
                return false;
            }
        }
        return false;
    }
}


