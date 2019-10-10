package fr.mineral.Events;

import fr.mineral.mineralcontest;
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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        //Player joueur = (Player) event.getEntity();
        //joueur.sendMessage("Degats: " + (event.getDamage()) + " - Vie: " + joueur.getHealth());



        if(mineralcontest.plugin.getGame().isGameStarted()) {

                if(event.getEntity() instanceof  Player) {
                    Player victime = (Player) event.getEntity();


                    if (victime.getHealth() - event.getDamage() < 0) {
                        victime.setHealth(20D);
                        event.setCancelled(true);



                        if(event.getDamager() instanceof Player) {
                            Player attaquant = (Player) event.getDamager();
                            mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + attaquant.getDisplayName() + " a tué " + victime.getDisplayName());
                        } else {
                            for(EntityDamageEvent.DamageCause raison :  EntityDamageEvent.DamageCause.values()) {
                                if(raison.equals(event.getCause())) {
                                    mineralcontest.plugin.getServer().broadcastMessage(mineralcontest.prefixGlobal + victime.getDisplayName() + "est décédé.");
                                }
                            }
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
    }
}
