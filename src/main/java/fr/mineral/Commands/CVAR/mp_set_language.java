package fr.mineral.Commands.CVAR;

import fr.mineral.Translation.Lang;
import fr.mineral.Translation.Language;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mp_set_language implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().contains("mp_set_language")) {
            if(sender.isOp()) {
                if(args.length == 1) {
                    for(Language item : Language.values()) {
                        if(args[0].equalsIgnoreCase(item.getLanguageName())) {
                            mineralcontest.plugin.LoadLangFile(args[0]);
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
        return false;
    }
}
