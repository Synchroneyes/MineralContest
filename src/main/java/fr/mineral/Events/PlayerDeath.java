package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = (Player) e.getEntity();


        List<ItemStack> inventaire = e.getDrops();
        ListIterator<ItemStack> iterateur = inventaire.listIterator();

        while(iterateur.hasNext()) {
            // On définit un itérateur pour parcourir la liste des items à drop
            ItemStack item = iterateur.next();

            // Liste des items à drop
            LinkedList<Material> item_a_drop = new LinkedList<Material>();
            item_a_drop.add(Material.IRON_INGOT);
            item_a_drop.add(Material.GOLD_INGOT);
            item_a_drop.add(Material.DIAMOND);
            item_a_drop.add(Material.EMERALD);

            // Si l'item actuelle n'est pas dans la liste, on le supprime de la liste de drop
            if(!item_a_drop.contains(item.getType())){
                iterateur.remove();
            }
        }

    }
}
