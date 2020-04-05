package fr.mapbuilder.Events;

import fr.mapbuilder.Blocks.BlocksDataColor;
import fr.mapbuilder.Items.ColoredHouseItem;
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


        /*if(itemName.equalsIgnoreCase(BlueHouseItem.itemName)) {
            placedItem.setType(Material.AIR);
            placedItem.setAmount(0);

            Location spawnedItemLocation = event.getBlock().getLocation();
            House.spawn(spawnedItemLocation, BlueHouseItem.color, player);
            event.setCancelled(true);
        } else if(itemName.equalsIgnoreCase(RedHouseItem.itemName)) {
            placedItem.setType(Material.AIR);
            placedItem.setAmount(0);

            Location spawnedItemLocation = event.getBlock().getLocation();
            House.spawn(spawnedItemLocation, RedHouseItem.color, player);
            event.setCancelled(true);
        }else if(itemName.equalsIgnoreCase(YellowHouseItem.itemName)) {
            placedItem.setType(Material.AIR);
            placedItem.setAmount(0);

            Location spawnedItemLocation = event.getBlock().getLocation();
            House.spawn(spawnedItemLocation, YellowHouseItem.color, player);
            event.setCancelled(true);
        }*/
    }
}
