package fr.mapbuilder.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class SpawnHouse extends BukkitCommand{

    public static String pluginCommand = "spawnhouse";
    private String required_permission = "admin";

    public SpawnHouse() {
        super(pluginCommand);
    }

    public SpawnHouse(String name) {
        super(name);
        this.description = "Spawn a house";
        this.usageMessage = "/" + name;
        this.setPermission(required_permission);
        this.setPermissionMessage("Admin only");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(!commandSender.hasPermission(required_permission)) {
            commandSender.sendMessage(ChatColor.RED + this.getPermissionMessage());
            return true;
        }


        commandSender.sendMessage("Building a new house right now ...");
        return false;
    }

}
