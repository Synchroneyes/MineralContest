package fr.synchroneyes.mineral.Commands.Developper;

import fr.synchroneyes.mineral.Utils.Setup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ValiderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player joueur = (Player) sender;

        joueur.setFlySpeed(1f);
        if (Setup.getEtape() > 0 && Setup.premierLancement) {

            if (Setup.Joueur.equals(joueur)) {
                Setup.validerChoix();
            }
        }
        return false;
    }
}
