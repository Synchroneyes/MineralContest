package fr.mineral.Events;

import fr.mineral.Utils.Radius;
import fr.mineral.mineralcontest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ChestEvent implements Listener {

    // Lorsqu'on ferme un inventaire
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) throws Exception {
        // Si l'inventaire est un coffre
        if(event.getInventory().getHolder() instanceof Chest) {

            Chest c = (Chest) event.getInventory().getHolder();
            Location coffreArene = mineralcontest.plugin.getCoffre().getPosition();

            // Si le coffre est celui de l'arene
            if(Radius.isBlockInRadius(coffreArene, c.getBlock().getLocation(), 1)) {
                // On le casse
                c.getBlock().breakNaturally();
            }

        }
    }


    @EventHandler
    public void onChestBreaked(ItemSpawnEvent event) throws Exception {
        //mineralcontest.plugin.getServer().broadcastMessage();
        Location coffreArene = mineralcontest.plugin.getCoffre().getPosition();

        // On regarde si les items qui ont spawn sont proche du coffre de l'arene
        if(Radius.isBlockInRadius(coffreArene,event.getLocation(), 1)){
            // Si l'item est un coffre
            if(event.getEntity().getItemStack().equals(new ItemStack(Material.CHEST, 1)))
                // On ne le fait pas apparaitre
                event.setCancelled(true);
        }
    }
}
