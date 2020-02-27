package fr.mineral.Core.MapBuilder.Event;

import fr.mineral.Translation.Lang;
import fr.mineral.mineralcontest;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.EGG)) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            assert itemMeta != null;

            // We check if this is the correct house spawner egg
            if(itemMeta.getDisplayName().equals(Lang.map_builder_item_name.toString())) {
                // We remove it from main hand if creative
                if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                    event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                mineralcontest.plugin.getServer().broadcastMessage("HOUSE SPAWN WITH EGG");

            } else {
                mineralcontest.plugin.getServer().broadcastMessage("ERR: " + itemMeta.getDisplayName());

            }
        }
    }

}
