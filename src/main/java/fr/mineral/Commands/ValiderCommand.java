package fr.mineral.Commands;

import fr.mineral.Utils.Setup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValiderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(Setup.getEtape() > 0 && Setup.premierLancement) {
            Player joueur = (Player) sender;
            if(Setup.Joueur.equals(joueur)) {
                Setup.validerChoix();
            }
        }
        return false;
    }
}
