package fr.mapbuilder.Commands;

import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Items.ColoredHouseItem;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SpawnHouse extends BukkitCommand{

    public static String pluginCommand = "spawnhouse";
    private String required_permission = "admin";
    private static mineralcontest plugin = mineralcontest.plugin;

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

        for(BlocksDataColor color : BlocksDataColor.values()) {
            ColoredHouseItem house = new ColoredHouseItem(color);
            house.giveItemToPlayer((Player) commandSender);
        }

        return false;
    }

}
