package fr.mapbuilder.Commands;

import fr.mapbuilder.MapBuilder;
import fr.mineral.mineralcontest;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;

public class mcbuild extends BukkitCommand {

    public static String pluginCommand = "mcbuild";
    private String required_permission = "admin";
    private static mineralcontest plugin = mineralcontest.plugin;

    public mcbuild() {
        super(pluginCommand);
        this.setDescription("Mineral contest build commands");
        this.setPermission(required_permission);
        this.setPermissionMessage("Admin required");
        
    }

    private mcbuild(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
