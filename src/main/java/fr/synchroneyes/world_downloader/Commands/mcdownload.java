package fr.synchroneyes.world_downloader.Commands;

import fr.synchroneyes.groups.Commands.CommandTemplate;
import fr.synchroneyes.mineral.mineralcontest;
import fr.synchroneyes.world_downloader.WorldDownloader;
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
    public boolean performCommand(CommandSender commandSender, String command, String[] args) {
        Player joueur = (Player) commandSender;


        if (!WorldDownloader.areMapsLoaded) {
            joueur.sendMessage(mineralcontest.prefixErreur + "Getting all maps, please wait ...");
            return false;
        }

        joueur.closeInventory();
        joueur.openInventory(WorldDownloader.getInstance().getInventory());

        return false;
    }

    @Override
    public String getCommand() {
        return "mcdownloader";
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
