package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDestroyed implements Listener {

    @EventHandler
    public void onBlockDestroyed(BlockBreakEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);
    }
}
