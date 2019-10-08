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


            /*
            On donne au joueur:
                - Arc
                - 64 fleches
                - Epée en fer
                -
             */
                joueur.getInventory().addItem(new ItemStack(Material.BOW, 1));
                joueur.getInventory().addItem(new ItemStack(Material.ARROW, 64));
                joueur.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));

                // On protege le joueur
                ItemStack[] armure = new ItemStack[4];
                armure[0] = new ItemStack(Material.IRON_BOOTS, 1);
                armure[1] = new ItemStack(Material.IRON_LEGGINGS, 1);
                armure[2] = new ItemStack(Material.IRON_CHESTPLATE, 1);
                armure[3] = new ItemStack(Material.IRON_HELMET, 1);

                joueur.getInventory().setArmorContents(armure);
            }
        }catch(Exception err) {
            joueur.sendMessage(mineralcontest.prefixErreur + err.getMessage());
        }


    }
}
