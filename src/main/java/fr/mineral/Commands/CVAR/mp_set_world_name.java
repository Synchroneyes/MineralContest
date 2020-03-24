package fr.mineral.Commands.CVAR;

import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class mp_set_world_name implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().equalsIgnoreCase("mp_set_world_name")) {
                if(commandSender.isOp()) {
                    if(strings.length == 1) {
                        String world_name = strings[0];
                        try {
                            GameSettingsCvar.setValueFromCVARName("world_name", world_name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        commandSender.sendMessage(mineralcontest.prefixPrive + "Plugin will now run on world " + world_name);
                    }
                }
            }
        }

        return false;
    }
}
