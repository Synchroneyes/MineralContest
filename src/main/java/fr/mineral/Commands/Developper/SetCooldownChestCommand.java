package fr.mineral.Commands.Developper;

import fr.mineral.Core.Arena.CoffreAvecCooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCooldownChestCommand implements CommandExecutor {

    /*
        dev command
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cooldownchest")) {
            try {
                CoffreAvecCooldown c = new CoffreAvecCooldown(((Player)sender).getLocation());
                c.spawn();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
