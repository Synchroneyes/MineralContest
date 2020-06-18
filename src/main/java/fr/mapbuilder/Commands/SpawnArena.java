package fr.mapbuilder.Commands;

import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Items.AreneItem;
import fr.mapbuilder.Items.ColoredHouseItem;
import fr.mineral.mineralcontest;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SpawnArena extends BukkitCommand {

    public static String pluginCommand = "spawnarene";
    private String required_permission = "admin";
    private static mineralcontest plugin = mineralcontest.plugin;

    public SpawnArena() {
        super(pluginCommand);
    }

    public SpawnArena(String name) {
        super(name);
        this.description = "Spawn l'arene";
        this.usageMessage = "/" + name;
        this.setPermission(required_permission);
        this.setPermissionMessage("Admin only");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!commandSender.hasPermission(required_permission)) {
            commandSender.sendMessage(ChatColor.RED + this.getPermissionMessage());
            return true;
        }

        AreneItem item = new AreneItem();
        item.giveItemToPlayer((Player) commandSender);

        return false;
    }

}
