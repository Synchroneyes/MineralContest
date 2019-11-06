package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class EntityDamage implements Listener {


    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (mineralcontest.plugin.getGame().isGameStarted() && event.getEntity() instanceof Player) {
            Player victime = (Player) event.getEntity();

            if (victime.getHealth() - event.getDamage() < 0) {
                victime.setHealth(20D);
                event.setCancelled(true);

                mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + mineralcontest.plugin.getGame().getPlayerTeam(victime).getCouleur() + victime.getDisplayName() + ChatColor.WHITE + " est décédé.");
                mineralcontest.plugin.getGame().getArene().getDeathZone().add(victime);

            }
        }
    }

    @EventHandler
    public boolean onEntityDamage(EntityDamageByEntityEvent event) {
        //Player joueur = (Player) event.getEntity();
        //joueur.sendMessage("Degats: " + (event.getDamage()) + " - Vie: " + joueur.getHealth());



        if(mineralcontest.plugin.getGame().isGameStarted()) {

                if(event.getEntity() instanceof  Player) {

                    Player victime = (Player) event.getEntity();

                    if(mineralcontest.plugin.getGame().getArene().getDeathZone().isPlayerDead(victime)){
                        event.setCancelled(true);
                        return true;
                    }


                    victime.sendMessage("AIE !");

                    if (victime.getHealth() - event.getDamage() < 0) {
                        victime.setHealth(20D);
                        event.setCancelled(true);



                        if(event.getDamager() instanceof Player) {
                            Player attaquant = (Player) event.getDamager();
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + mineralcontest.plugin.getGame().getPlayerTeam(attaquant).getCouleur() + attaquant.getDisplayName() + ChatColor.WHITE + " a tué " + mineralcontest.plugin.getGame().getPlayerTeam(victime).getCouleur() + victime.getDisplayName());
                        }



                        try {
                            for (ItemStack item : victime.getInventory().getContents()) {
                                if (item.isSimilar(new ItemStack(Material.IRON_INGOT, 1)))
                                    victime.getWorld().dropItemNaturally(victime.getLocation(), item);

                                if (item.isSimilar(new ItemStack(Material.GOLD_INGOT, 1)))
                                    victime.getWorld().dropItemNaturally(victime.getLocation(), item);

                                if (item.isSimilar(new ItemStack(Material.DIAMOND, 1)))
                                    victime.getWorld().dropItemNaturally(victime.getLocation(), item);

                                if (item.isSimilar(new ItemStack(Material.EMERALD, 1)))
                                    victime.getWorld().dropItemNaturally(victime.getLocation(), item);
                            }
                        }catch(Exception e){

                        }

                        mineralcontest.plugin.getGame().getArene().getDeathZone().add(victime);

                    }
                }
        }
        return false;
    }
}
