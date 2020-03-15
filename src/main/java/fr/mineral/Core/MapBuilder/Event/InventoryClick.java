package fr.mineral.Core.MapBuilder.Event;

import fr.mineral.Core.MapBuilder.MapBuilderInventory;
import fr.mineral.Core.MapBuilder.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class InventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        MapBuilderInventory mapBuilderInventory = MapBuilderInventory.getInstance();
        Player player = (Player) event.getWhoClicked();
        Logger log = Bukkit.getLogger();
        ItemStack clickedItem;
        /*
            If click item is from the mapbuilder inventory
         */


        if(inventory.equals(mapBuilderInventory.getInventory())) {
            int itemClickedSlot = event.getSlot();
            if((clickedItem = inventory.getItem(itemClickedSlot)) != null) {
                log.info(player.getDisplayName() + " clicked on an item (" + clickedItem.getType().toString() + ") from the map builder inventory");
                Utils.itemStackConverter(clickedItem, player);
                event.setCancelled(true);
            }
        }
    }
}
