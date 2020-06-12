package fr.world_downloader.Commands;

import fr.groups.Commands.CommandTemplate;
import fr.mineral.mineralcontest;
import fr.world_downloader.WorldDownloader;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mcdownload extends CommandTemplate {

    public mcdownload() {
        accessCommande.add(PLAYER_COMMAND);
        accessCommande.add(PLAYER_ADMIN);
        accessCommande.add(PLAYER_IN_HUB);
        constructArguments();
    }

    @Override
    public String getCommand() {
        return "mcdownloader";
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        try {
            canPlayerUseCommand(commandSender, strings);
        } catch (Exception e) {
            commandSender.sendMessage(mineralcontest.prefixErreur + e.getMessage());
            return false;
        }

        Player joueur = (Player) commandSender;
        joueur.closeInventory();
        joueur.openInventory(WorldDownloader.getInstance().getInventory());

        return false;
    }

    @Override
    public String getDescription() {
        return "Permet d'ouvrir le menu de télécargement de map";
    }

    @Override
    public String getPermissionRequise() {
        return null;
    }
}
