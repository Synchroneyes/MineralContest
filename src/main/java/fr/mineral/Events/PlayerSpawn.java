package fr.mineral.Events;

import fr.mineral.Teams.Equipe;
import fr.mineral.mineralcontest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSpawn implements Listener {

    public PlayerSpawn() {

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) throws Exception {
        Player joueur = (Player) e.getPlayer();
        joueur.sendMessage("Revie !");
        // Si le joueur est dans une équipe, alors on le spawn dans sa maison

        try {
            if(mineralcontest.plugin.getPlayerTeam(joueur) != null) {
                Location house = mineralcontest.plugin.getPlayerTeam(joueur).getHouseLocation();
                Bukkit.getScheduler().scheduleSyncDelayedTask(mineralcontest.plugin, new Runnable() {
                    public void run() {
                        joueur.teleport(house); //send the message "test"
                    }
                }, 5);


            }
        }catch(Exception err) {
            joueur.sendMessage(mineralcontest.prefixErreur + err.getMessage());
        }


    }
}
