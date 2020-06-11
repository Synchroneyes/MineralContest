package fr.mapbuilder.Events;

import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Items.AreneItem;
import fr.mapbuilder.Items.ColoredHouseItem;
import fr.mapbuilder.Spawner.Arene;
import fr.mapbuilder.Spawner.House;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaced implements Listener {
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        ItemStack placedItem = event.getItemInHand();
        String itemName = placedItem.getItemMeta().getDisplayName();
        Player player = event.getPlayer();

        if(itemName.contains(ColoredHouseItem.itemPrefix)) {
            placedItem.setType(Material.AIR);
            Location spawnItemLocation = event.getBlock().getLocation();
            House.spawn(spawnItemLocation, BlocksDataColor.fromItemName(itemName), player);
            event.setCancelled(true);
        }

        if (itemName.equalsIgnoreCase(AreneItem.itemPrefix)) {
            Arene.spawn(event.getBlock().getLocation(), player);
            event.setCancelled(true);
        }





    }
}
