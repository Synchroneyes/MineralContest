package fr.mineral.Events;

import fr.mineral.mineralcontest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockSpread implements Listener {
    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if(mineralcontest.plugin.getGame().isGamePaused())
            event.setCancelled(true);
    }
}
