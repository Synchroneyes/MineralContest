package fr.mineral.Commands;

import fr.mineral.Utils.AutomaticSetup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestSetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player joueur = (Player) sender;
        if(command.getName().equalsIgnoreCase("spawnarene")) {
            AutomaticSetup.setPositionSpawnArene(joueur.getLocation());
        }

        if(command.getName().equalsIgnoreCase("tpjaune")) {
            AutomaticSetup.teleportToJaune(joueur);
        }

        if(command.getName().equalsIgnoreCase("tprouge")) {
            AutomaticSetup.teleportToRouge(joueur);
        }

        if(command.getName().equalsIgnoreCase("tpbleu")) {
            AutomaticSetup.teleportToBleu(joueur);
        }

        return false;
    }
}
