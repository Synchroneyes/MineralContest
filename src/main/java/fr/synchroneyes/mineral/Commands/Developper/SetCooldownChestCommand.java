package fr.synchroneyes.mineral.Commands.Developper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetCooldownChestCommand implements CommandExecutor {

    /*
        dev command
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*if(command.getName().equals("cooldownchest")) {
            try {
                CoffreAvecCooldown c = new CoffreAvecCooldown(((Player)sender).getLocation());
                c.spawn();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }*/
        return false;
    }
}
