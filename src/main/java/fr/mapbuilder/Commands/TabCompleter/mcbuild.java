package fr.mapbuilder.Commands.TabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class mcbuild implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equals(fr.mapbuilder.Commands.mcbuild.pluginCommand)) {
            List<String> arguments = new ArrayList<>();
            arguments.add("set");
            arguments.add("add");
            return arguments;
        }
        return null;
    }
}
