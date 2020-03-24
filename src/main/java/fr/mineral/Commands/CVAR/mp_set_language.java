package fr.mineral.Commands.CVAR;

import fr.mineral.Core.GameSettings;
import fr.mineral.Core.GameSettingsCvar;
import fr.mineral.Translation.Lang;
import fr.mineral.Translation.Language;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class mp_set_language implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.getWorld().equals(mineralcontest.plugin.pluginWorld)) {
            if(command.getName().contains("mp_set_language")) {
                if(sender.isOp()) {
                    if(args.length == 1) {
                        for(Language item : Language.values()) {
                            if(args[0].equalsIgnoreCase(item.getLanguageName())) {

                                GameSettingsCvar.mp_set_language.setValue(args[0]);
                                //mineralcontest.mp_set_language = args[0];
                                Lang.loadLang((String) GameSettingsCvar.mp_set_language.getValue());
                            /*try {
                                mineralcontest.plugin.setConfigValue("config.cvar.mp_set_language", mineralcontest.mp_set_language);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/

                                return true;
                            }
                        }

                        sender.sendMessage("Available languages: " + Language.getAvailableLanguages());
                        return false;
                    }else{
                        sender.sendMessage("Usage: /mp_set_language <language name>");
                        return false;
                    }
                }
            }
        }

        return false;
    }
}
