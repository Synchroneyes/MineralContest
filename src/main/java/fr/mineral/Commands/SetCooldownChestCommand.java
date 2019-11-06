package fr.mineral.Commands;

import fr.mineral.Core.Arena.ChestWithCooldown;
import fr.mineral.mineralcontest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCooldownChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cooldownchest")) {
            try {
                ChestWithCooldown c = new ChestWithCooldown(((Player)sender).getLocation());
                c.spawn();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
