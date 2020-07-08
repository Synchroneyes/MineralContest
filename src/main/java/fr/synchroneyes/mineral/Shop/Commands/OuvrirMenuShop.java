package fr.synchroneyes.mineral.Shop.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import org.bukkit.command.CommandSender;

public class OuvrirMenuShop extends CommandTemplate {
    @Override
    public String getCommand() {
        return "mcshop";
    }

    @Override
    public String getDescription() {
        return "Ouvre le menu du shop";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }

    @Override
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {

        return false;
    }
}
