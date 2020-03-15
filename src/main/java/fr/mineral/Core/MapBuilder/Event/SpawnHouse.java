package fr.mineral.Core.MapBuilder.Event;

import fr.mineral.Core.MapBuilder.Item.HouseEgg;
import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnHouse implements Listener {

    /*
    TODO
     */
    @EventHandler
    public void onEggThrown(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand().getType().equals(Material.EGG)) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;

            // We check if this is the correct house spawner egg
            if(itemMeta.getDisplayName().equals(Lang.map_builder_item_name.toString())) {
                mineralcontest.plugin.getServer().broadcastMessage("HOUSE SPAWN WITH EGG");
                HouseEgg.spawnHouse(player);
            } else {
                mineralcontest.plugin.getServer().broadcastMessage("ERR: " + itemMeta.getDisplayName());

            }
        }
    }

}
