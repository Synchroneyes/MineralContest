package fr.synchroneyes.mineral.Commands;

import fr.synchroneyes.mineral.Core.Game.Game;
import fr.synchroneyes.mineral.Translation.Lang;
import fr.synchroneyes.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AllowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            Player joueur = (Player) commandSender;
            Game game = mineralcontest.getPlayerGame(joueur);
            if (game == null) {
                commandSender.sendMessage(mineralcontest.prefixErreur + Lang.error_command_can_only_be_used_in_game.toString());
                return false;
            }

            /*Location playerHighLocation = joueur.getLocation();
            playerHighLocation.setY(playerHighLocation.getBlockY()+100);
            Block pBlock = playerHighLocation.getBlock();
            pBlock.setType(Material.CHEST);

            FallingBlock b = joueur.getWorld().spawnFallingBlock(playerHighLocation, pBlock.getBlockData());
            b.setFireTicks(1);
            b.setInvulnerable(true);
            b.setHurtEntities(false);

            Bukkit.broadcastMessage(playerHighLocation + "");*/


            if (command.getName().equalsIgnoreCase("allow")) {
                if (commandSender.isOp()) {
                    if (args.length == 1) {
                        String playerName = args[0];
                        if (!game.allowPlayerLogin(playerName)) {
                            commandSender.sendMessage(playerName + " did not tried to join the game");
                            return false;
                        }

                        commandSender.sendMessage(playerName + " is now allowed to join");
                        return false;
                    } else {
                        commandSender.sendMessage("Usage: /allow <playername>");
                        return false;
                    }
                }
            }
        }


        return false;
    }
}
